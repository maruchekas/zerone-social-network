package com.skillbox.javapro21.service.impl;

import com.skillbox.javapro21.api.response.*;
import com.skillbox.javapro21.api.response.account.AuthData;
import com.skillbox.javapro21.domain.*;
import com.skillbox.javapro21.domain.enumeration.FriendshipStatusType;
import com.skillbox.javapro21.repository.*;
import com.skillbox.javapro21.service.NotificationService;
import com.skillbox.javapro21.service.kbLayearConverter.KbLayerConverter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.skillbox.javapro21.domain.enumeration.FriendshipStatusType.*;
import static com.skillbox.javapro21.domain.enumeration.NotificationType.FRIEND_REQUEST;

@Setter
@RequiredArgsConstructor
@Component
public class UtilsService {
    private final PersonRepository personRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipStatusRepository friendshipStatusRepository;
    private final NotificationRepository notificationRepository;
    private NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * поиск пользователя по почте, если не найден выбрасывает ошибку
     */
    public Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * используется для ответа 200 "message: ok"
     */
    public DataResponse<MessageOkContent> getMessageOkResponse() {
        DataResponse<MessageOkContent> dataResponse = new DataResponse<>();
        dataResponse.setTimestamp(getTimestamp());
        MessageOkContent accountData = new MessageOkContent();
        accountData.setMessage("ok");
        dataResponse.setData(accountData);
        return dataResponse;
    }

    /**
     * Шаблон для DataResponse
     */
    public DataResponse<Content> getDataResponse(Content data) {
        return new DataResponse<>()
                .setError("ok")
                .setTimestamp(getTimestamp())
                .setData(data);
    }

    /**
     * Шаблон для ListDataResponse
     */
    public ListDataResponse<Content> getListDataResponse(int total, int offset, int limit, List<Content> data) {
        return new ListDataResponse<>()
                .setError("ok")
                .setTimestamp(getTimestamp())
                .setTotal(total)
                .setOffset(offset)
                .setPerPage(limit)
                .setData(data);
    }

    /**
     * Шаблон для StringListDataResponse
     */
    public StringListDataResponse getStringListDataResponse(List<String> dataList) {
        return new StringListDataResponse()
                .setError("ok")
                .setTimestamp(getTimestamp())
                .setData(dataList);
    }

    /**
     * создание рандомного токена
     */
    public String getToken() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    /**
     * заблокирован пользователь или нет ?
     */
    public String isBlockedPerson(Person person) {
        return person.getIsBlocked() == 0 ? "false" : "true";
    }

    /**
     * заполнение данных о пользователе
     */
    public AuthData getAuthData(Person person, String token) {

        AuthData authData = new AuthData()
                .setId(person.getId())
                .setFirstName(person.getFirstName())
                .setLastName(person.getLastName())
                .setRegDate(person.getRegDate().toInstant(ZoneOffset.UTC).toEpochMilli())
                .setEmail(person.getEmail())
                .setMessagePermission(person.getMessagesPermission())
                .setLastOnlineTime(person.getLastOnlineTime().toInstant(ZoneOffset.UTC).toEpochMilli())
                .setBlocked(isBlockedPerson(person).equals("true"))
                .setToken(token);
        if (person.getPhone() != null) authData.setPhone(person.getPhone());
        if (person.getPhoto() != null) authData.setPhoto(person.getPhoto());
        if (person.getAbout() != null) authData.setAbout(person.getAbout());
        if (person.getCountry() != null) authData.setCountry(setCountryData(person));
        if (person.getTown() != null) authData.setCity(setCityData(person));
        if (person.getBirthDate() != null)
            authData.setBirthDate(person.getBirthDate().toInstant(ZoneOffset.UTC).toEpochMilli());
        return authData;
    }

    /**
     * получение LocalDateTime из TimestampAccessor, который отдает фронт
     */
    public LocalDateTime getLocalDateTime(long dateWithTimestampAccessor) {
        return LocalDateTime.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(dateWithTimestampAccessor)),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public LocalDateTime getLocalDateTimeZoneOffsetUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Получение Timestamp
     */
    public long getTimestamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public long getTimestampFromLocalDateTime(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * проверка пользователей на статусы блокировок
     */
    public boolean isBlockedBy(Long blocker, Long blocked, Optional<Friendship> optional) {
        return optional.filter(friendship ->
                (blocker == friendship.getSrcPerson().getId() && friendship.getFriendshipStatus().getFriendshipStatusType().equals(FriendshipStatusType.BLOCKED))
                        || (blocked == friendship.getSrcPerson().getId() && friendship.getFriendshipStatus().getFriendshipStatusType().equals(WASBLOCKED))
                        || friendship.getFriendshipStatus().getFriendshipStatusType().equals(INTERLOCKED)).isEmpty();
    }

    /**
     * создание отношений между пользователями
     */
    @Transactional
    public void createFriendship(Person src, Person dst, FriendshipStatusType friendshipStatusType) {
        switch (friendshipStatusType) {
            case BLOCKED -> setFriendshipStatusBlocked(src, dst);
            case INTERLOCKED -> setFriendshipStatusBlocked(dst, src);
            case FRIEND, WASBLOCKED -> setOneFriendshipStatusTypeForSrcAndDst(src, dst, friendshipStatusType);
            case DECLINED, SUBSCRIBED, REQUEST -> setFriendshipStatusTypeForSrc(src, dst, friendshipStatusType);
        }
    }

    private void setFriendshipStatusBlocked(Person src, Person dst) {
        saveNewFriendshipForSrcAndDst(src, dst, BLOCKED);
        saveNewFriendshipForSrcAndDst(dst, src, WASBLOCKED);
    }

    private void setFriendshipStatusTypeForSrc(Person src, Person dst, FriendshipStatusType fst) {
        if (fst.equals(DECLINED)) {
            saveNewFriendshipForSrcAndDst(src, dst, fst);
            saveNewFriendshipForSrcAndDst(dst, src, SUBSCRIBED);
        } else {
            saveNewFriendshipForSrcAndDst(src, dst, fst);
        }
    }

    private void setOneFriendshipStatusTypeForSrcAndDst(Person src, Person dst, FriendshipStatusType friendshipStatusType) {
        FriendshipStatusType fst = null;
        if (friendshipStatusType.equals(WASBLOCKED)) {
            fst = INTERLOCKED;
        } else if (friendshipStatusType.equals(FRIEND)) {
            fst = FRIEND;
            notificationService.checkBirthdayFromOneAndCreateNotificationToAnotherInCase(src, dst);
            notificationService.checkBirthdayFromOneAndCreateNotificationToAnotherInCase(dst, src);
        }
        saveNewFriendshipForSrcAndDst(src, dst, fst);
        saveNewFriendshipForSrcAndDst(dst, src, fst);
    }

    /**
     * установка данных о стране и городе пользователя
     */
    private Map<String, String> setCountryData(Person person) {
        Country country = countryRepository.findCountryByName(person.getCountry()).orElse(null);

        long countryId = country == null ? person.getId() : country.getId();
        String countryName = country == null ? person.getCountry() : country.getName();
        return Map.of("id", String.valueOf(countryId), "Country", countryName);
    }

    private Map<String, String> setCityData(Person person) {
        City city = cityRepository.findCityByName(person.getTown()).orElse(null);

        long cityId = city == null ? person.getId() : city.getId();
        String cityName = city == null ? person.getTown() : city.getName();
        return Map.of("id", String.valueOf(cityId), "City", cityName);
    }

    private void saveNewFriendshipForSrcAndDst(Person src, Person dst, FriendshipStatusType fst) {
        FriendshipStatus friendshipStatus = getFriendshipStatus(src.getId(), dst.getId());
        if (friendshipStatus != null) {
            FriendshipStatus friendshipStatusSrcAfterSave = saveFriendshipStatus(friendshipStatus, fst);
            Friendship friendshipSrc = friendshipRepository.findFriendshipBySrcPersonAndDstPerson(src.getId(), dst.getId()).orElseThrow();
            saveFriendship(friendshipSrc, src, dst, friendshipStatusSrcAfterSave);
        } else {
            createNewFriendship(src, dst, fst);
        }
    }


    private FriendshipStatus saveFriendshipStatus(FriendshipStatus friendshipStatus, FriendshipStatusType type) {
        friendshipStatus.setFriendshipStatusType(type).setTime(LocalDateTime.now(ZoneOffset.UTC));
        return friendshipStatusRepository.save(friendshipStatus);
    }

    private void createNewFriendship(Person src, Person dst, FriendshipStatusType type) {
        FriendshipStatus friendshipStatus = new FriendshipStatus()
                .setFriendshipStatusType(type)
                .setTime(LocalDateTime.now(ZoneOffset.UTC));
        FriendshipStatus saveFSSrc = friendshipStatusRepository.save(friendshipStatus);
        Friendship friendshipSrc = new Friendship()
                .setSrcPerson(src)
                .setDstPerson(dst)
                .setFriendshipStatus(saveFSSrc);
        friendshipRepository.save(friendshipSrc);

        if (type == REQUEST) {
            notificationRepository.save(new Notification()
                    .setSentTime(getLocalDateTimeZoneOffsetUtc())
                    .setNotificationType(FRIEND_REQUEST)
                    .setPerson(dst)
                    .setEntityId(friendshipSrc.getId())
                    .setContact("Contact"));

            WSNotificationResponse response = new WSNotificationResponse();
            response.setNotificationType(FRIEND_REQUEST);
            response.setInitiatorName(dst.getEmail());
            simpMessagingTemplate.convertAndSendToUser(src.getEmail(), "/topic/notifications", response);
        }
    }

    /**
     * метод поиска статуса искателя к искомому
     */
    public FriendshipStatus getFriendshipStatus(Long p1, Long p2) {
        return friendshipStatusRepository.findFriendshipStatusByPersonsSrcAndDstId(p1, p2);
    }

    private void saveFriendship(Friendship friendship, Person src, Person dst, FriendshipStatus friendshipStatusSrcAfterSave) {
        friendship.setSrcPerson(src);
        friendship.setDstPerson(dst);
        friendship.setFriendshipStatus(friendshipStatusSrcAfterSave);
        friendshipRepository.save(friendship);
    }

    /**
     * метод конвертации текста введенного в неверной раскладке клавиатуры
     * example: Ghbdtn vbh! -> Привет мир!
     */

    public String convertKbLayer(String input) {
        KbLayerConverter kbLayerConverter = new KbLayerConverter();
        return kbLayerConverter.convertString(input);
    }
}

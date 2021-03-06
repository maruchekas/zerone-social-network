package com.skillbox.javapro21.service.impl;

import com.skillbox.javapro21.api.response.statistics.*;
import com.skillbox.javapro21.domain.Person;
import com.skillbox.javapro21.domain.Post;
import com.skillbox.javapro21.domain.PostComment;
import com.skillbox.javapro21.domain.PostLike;
import com.skillbox.javapro21.domain.marker.ForStream;
import com.skillbox.javapro21.repository.PersonRepository;
import com.skillbox.javapro21.repository.PostCommentRepository;
import com.skillbox.javapro21.repository.PostLikeRepository;
import com.skillbox.javapro21.repository.PostRepository;
import com.skillbox.javapro21.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final PersonRepository personRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    private final static int MONTH = 12;
    private final static int HOURS = 12;

    @Override
    public StatisticsResponse getAllStatistic() {
        Long countPersons = personRepository.findCountPerson();
        Long countLikes = postLikeRepository.count();
        Long countPosts = postRepository.findCountPosts();
        Long countComments = postCommentRepository.findCountComments();
        return new StatisticsResponse()
                .setUsersCount(countPersons)
                .setPostsCount(countPosts)
                .setLikesCount(countLikes)
                .setCommentsCount(countComments);
    }

    @Override
    public PostStatResponse getPostsStatistic() {
        Long countPosts = postRepository.findCountPosts();
        List<Post> postList = postRepository.allPosts();
        Map<YearMonth, Long> posts = getMapForAllStat(postList);
        Map<Integer, Long> postsByHour = getMapForHourStat(postList);
        return new PostStatResponse()
                .setCountPosts(countPosts)
                .setPosts(posts)
                .setPostsByHour(postsByHour);
    }

    @Override
    public UsersStatResponse getUsersStatistic() {
        Long countPersons = personRepository.findCountPerson();
        List<Person> personList = personRepository.findAllPersons();
        Map<LocalDate, Long> dynamic = new TreeMap<>();
        YearsUsersStat yearsUsersStat = new YearsUsersStat()
                .setYoung(getPercentPersonsByYearsOld(0, 18))
                .setTeenager(getPercentPersonsByYearsOld(18, 25))
                .setAdult(getPercentPersonsByYearsOld(25, 45))
                .setElderly(getPercentPersonsByYearsOld(45, 1000));
        for (int i = 0; i < 10; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().minusDays(10 - i);
            LocalDate localDate = localDateTime.toLocalDate();
            long count = personList.stream()
                    .filter(p -> p.getRegDate().getDayOfMonth() == (localDateTime.getDayOfMonth()))
                    .count();
            dynamic.put(localDate, count);
        }
        return new UsersStatResponse()
                .setUsersCount(countPersons)
                .setDynamic(dynamic)
                .setYearsUsersStat(yearsUsersStat);
    }

    @Override
    public CommentsStatResponse getCommentsStatistic() {
        Long countComments = postCommentRepository.findCountComments();
        List<PostComment> commentsList = postCommentRepository.allComments();
        Map<YearMonth, Long> comments = getMapForAllStat(commentsList);
        Map<Integer, Long> commentsByHour = getMapForHourStat(commentsList);
        return new CommentsStatResponse()
                .setCommentsCount(countComments)
                .setComments(comments)
                .setCommentsByHour(commentsByHour);
    }

    @Override
    public LikesStatResponse getLikesStatistic() {
        Long countLikes = postLikeRepository.findCountLikes();
        List<PostLike> likesList = postLikeRepository.findAll();
        Map<YearMonth, Long> likes = getMapForAllStat(likesList);
        Map<Integer, Long> likesByHour = getMapForHourStat(likesList);
        return new LikesStatResponse()
                .setLikesCount(countLikes)
                .setLikes(likes)
                .setLikesByHour(likesByHour);
    }

    private Map<YearMonth, Long> getMapForAllStat(List<? extends ForStream> list) {
        Map<YearMonth, Long> allStat = new TreeMap<>();
        for (int i = 0; i <= MONTH; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().minusMonths(MONTH - i);
            YearMonth month = YearMonth.of(localDateTime.getYear(), localDateTime.getMonth());
            long count = list.stream()
                    .filter(a -> a.getTime().getMonth().equals(localDateTime.getMonth()))
                    .count();
            allStat.put(month, count);
        }
        return allStat;
    }

    private Map<Integer, Long> getMapForHourStat(List<? extends ForStream> list) {
        Map<Integer, Long> allHourStat = new TreeMap<>();
        for (int i = 0; i <= HOURS; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().minusHours(HOURS - i);
            Integer time = localDateTime.toLocalTime().getHour();
            long count = list.stream()
                    .filter(p -> p.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
                    .filter(p -> p.getTime().getHour() == (localDateTime.getHour()))
                    .count();
            allHourStat.put(time, count);
        }
        return allHourStat;
    }

    private Integer getPercentPersonsByYearsOld(int from, int before) {
        LocalDateTime fromTime = LocalDateTime.now().minusYears(from);
        LocalDateTime beforeTime = LocalDateTime.now().minusYears(before);
        return personRepository.findAllPersonsByYearsOld(beforeTime, fromTime).size();
    }
}

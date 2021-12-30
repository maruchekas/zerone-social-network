package com.skillbox.javapro21.service;

import com.skillbox.javapro21.api.request.post.PostRequest;
import com.skillbox.javapro21.api.request.profile.EditProfileRequest;
import com.skillbox.javapro21.api.response.DataResponse;
import com.skillbox.javapro21.api.response.ListDataResponse;
import com.skillbox.javapro21.api.response.MessageOkContent;
import com.skillbox.javapro21.api.response.account.AuthData;
import com.skillbox.javapro21.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface ProfileService {
    DataResponse getPersonById(long id) throws PersonNotFoundException;
    DataResponse getPerson(Principal principal);
    DataResponse post(long id, long publishDate, PostRequest postRequest) throws PersonNotFoundException;
    ListDataResponse getWall(long id, int offset, int itemPerPage) throws PersonNotFoundException;
    DataResponse<AuthData> editPerson(Principal principal, EditProfileRequest editProfileRequest);
    DataResponse<MessageOkContent> deletePerson(Principal principal);
}

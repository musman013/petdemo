package com.fastcode.demopet.application.authorization.user;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.PermalinkEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.application.authorization.user.dto.*;

import java.util.List;

@Service
public interface IUserAppService {

	CreateUserOutput create(CreateUserInput user);

    void delete(Long id);

    UpdateUserOutput update(Long userId, UpdateUserInput input);

    FindUserByIdOutput findById(Long id);

    List<FindUserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	FindUserByNameOutput findByUserName(String userName);

	FindUserByNameOutput findByEmailAddress(String emailAddress);

	Boolean checkIsAdmin(UserEntity user);

	FindUserWithAllFieldsByIdOutput findWithAllFieldsById(Long valueOf);

	UserEntity getUser();

}

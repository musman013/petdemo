package com.fastcode.demopet.application.authorization.user;

import com.fastcode.demopet.application.authorization.user.dto.*;
import com.fastcode.demopet.domain.model.UserEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    /*
    CreateUserInput => User
    User => CreateUserOutput
    UpdateUserInput => User
    User => UpdateUserOutput
    User => FindUserByIdOutput
    Permission => GetPermissionOutput
    Role => GetRoleOutput
     */

    UserEntity createUserInputToUserEntity(CreateUserInput userDto);
   
    CreateUserOutput userEntityToCreateUserOutput(UserEntity entity);
    
    @Mappings({
    	@Mapping(source = "userProfile.userName", target = "userName"),
    	@Mapping(source = "userProfile.emailAddress", target = "emailAddress"),
    	@Mapping(source = "userProfile.phoneNumber", target = "phoneNumber"),
    	@Mapping(source = "userProfile.lastName", target = "lastName"),
    	@Mapping(source = "userProfile.firstName", target = "firstName")
    })
    UpdateUserInput findUserWithAllFieldsByIdOutputAndUserProfileToUpdateUserInput(FindUserWithAllFieldsByIdOutput user, UserProfile userProfile);
    
    UserProfile updateUserOutputToUserProfile(UpdateUserOutput userDto);
    
    UserProfile findUserByIdOutputToUserProfile(FindUserByIdOutput user);
    UserEntity updateUserInputToUserEntity(UpdateUserInput userDto);

    UpdateUserOutput userEntityToUpdateUserOutput(UserEntity entity);

    FindUserByIdOutput userEntityToFindUserByIdOutput(UserEntity entity);
     
    FindUserByNameOutput userEntityToFindUserByNameOutput(UserEntity entity);

    FindUserWithAllFieldsByIdOutput userEntityToFindUserWithAllFieldsByIdOutput(UserEntity entity);
  
}
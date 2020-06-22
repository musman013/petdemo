package com.fastcode.demopet.application.owners;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserOutput;
import com.fastcode.demopet.application.authorization.user.dto.UserProfile;
import com.fastcode.demopet.application.owners.dto.*;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.UserEntity;

@Mapper(componentModel = "spring")
public interface IOwnersMapper {

   OwnersEntity createOwnersInputToOwnersEntity(CreateOwnersInput ownersDto);
   
   @Mapping(source = "entity.user.id" , target ="id" )
   CreateOwnersOutput ownersEntityAndUserEntityToCreateOwnersOutput(OwnersEntity entity, UserEntity user);

   OwnersEntity updateOwnersInputToOwnersEntity(UpdateOwnersInput ownersDto);

   @Mapping(source = "entity.user.id" , target ="id" )
   UpdateOwnersOutput ownersEntityAndUserEntityToUpdateOwnersOutput(OwnersEntity entity, UserEntity user);

   @Mappings({
   @Mapping(source = "entity.user.id" , target = "id" ),
   @Mapping(source = "entity.version" , target = "version" )
   })
   FindOwnersByIdOutput ownersEntityAndUserEntityToFindOwnersByIdOutput(OwnersEntity entity, UserEntity user);

   OwnerProfile findOwnersByIdOutputToOwnerProfile(FindOwnersByIdOutput owner);
   
   @Mappings({
   	@Mapping(source = "ownerProfile.userName", target = "userName"),
   	@Mapping(source = "ownerProfile.emailAddress", target = "emailAddress"),
   	@Mapping(source = "ownerProfile.phoneNumber", target = "phoneNumber"),
   	@Mapping(source = "ownerProfile.lastName", target = "lastName"),
   	@Mapping(source = "ownerProfile.firstName", target = "firstName"),
   	@Mapping(source = "ownerProfile.address", target = "address"),
   	@Mapping(source = "ownerProfile.city", target = "city")
   })
   UpdateOwnersInput findUserWithAllFieldsByIdOutputAndOwnerProfileToUpdateOwnerInput(FindUserWithAllFieldsByIdOutput user, OwnerProfile ownerProfile);
   
   OwnerProfile updateOwnerOutputToOwnerProfile(UpdateOwnersOutput ownerDto);
   
}

package com.fastcode.demopet.application.vets;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;

@Mapper(componentModel = "spring")
public interface IVetsMapper {

   VetsEntity createVetsInputToVetsEntity(CreateVetsInput vetsDto);
   
   @Mapping(source = "entity.id", target = "id")
   CreateVetsOutput vetsEntityAndUserEntityToCreateVetsOutput(VetsEntity entity, UserEntity user);

   VetsEntity updateVetsInputToVetsEntity(UpdateVetsInput vetsDto);

   @Mapping(source = "entity.id", target = "id")
   UpdateVetsOutput vetsEntityAndUserEntityToUpdateVetsOutput(VetsEntity entity, UserEntity user);

   @Mappings({
   @Mapping(source = "entity.id", target = "id"),
   @Mapping(source = "entity.version", target = "version")
   })
   FindVetsByIdOutput vetsEntityAndUserEntityToFindVetsByIdOutput(VetsEntity entity, UserEntity user);

   VetProfile findVetsByIdOutputToVetProfile(FindVetsByIdOutput vet);
   
   VetProfile updateVetsOutputToVetProfile(UpdateVetsOutput VetDto);
   
   @Mappings({
   	@Mapping(source = "vetProfile.userName", target = "userName"),
   	@Mapping(source = "vetProfile.emailAddress", target = "emailAddress"),
   	@Mapping(source = "vetProfile.phoneNumber", target = "phoneNumber"),
   	@Mapping(source = "vetProfile.lastName", target = "lastName"),
   	@Mapping(source = "vetProfile.firstName", target = "firstName")
   })
   UpdateVetsInput findUserWithAllFieldsByIdOutputAndVetProfileToUpdateVetInput(FindUserWithAllFieldsByIdOutput user, VetProfile vetProfile);
}

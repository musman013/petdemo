package com.fastcode.demo.application.owners;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demo.application.owners.dto.*;
import com.fastcode.demo.domain.model.OwnersEntity;

@Mapper(componentModel = "spring")
public interface IOwnersMapper {

   OwnersEntity createOwnersInputToOwnersEntity(CreateOwnersInput ownersDto);
   
   CreateOwnersOutput ownersEntityToCreateOwnersOutput(OwnersEntity entity);

	
    OwnersEntity updateOwnersInputToOwnersEntity(UpdateOwnersInput ownersDto);

   UpdateOwnersOutput ownersEntityToUpdateOwnersOutput(OwnersEntity entity);

   FindOwnersByIdOutput ownersEntityToFindOwnersByIdOutput(OwnersEntity entity);


}

package com.fastcode.demopet.application.vets;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.domain.model.VetsEntity;

@Mapper(componentModel = "spring")
public interface IVetsMapper {

   VetsEntity createVetsInputToVetsEntity(CreateVetsInput vetsDto);
   
   CreateVetsOutput vetsEntityToCreateVetsOutput(VetsEntity entity);

	
    VetsEntity updateVetsInputToVetsEntity(UpdateVetsInput vetsDto);

   UpdateVetsOutput vetsEntityToUpdateVetsOutput(VetsEntity entity);

   FindVetsByIdOutput vetsEntityToFindVetsByIdOutput(VetsEntity entity);


}

package com.fastcode.demopet.application.specialties;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.application.specialties.dto.*;
import com.fastcode.demopet.domain.model.SpecialtiesEntity;

@Mapper(componentModel = "spring")
public interface ISpecialtiesMapper {

   SpecialtiesEntity createSpecialtiesInputToSpecialtiesEntity(CreateSpecialtiesInput specialtiesDto);
   
   CreateSpecialtiesOutput specialtiesEntityToCreateSpecialtiesOutput(SpecialtiesEntity entity);

	
    SpecialtiesEntity updateSpecialtiesInputToSpecialtiesEntity(UpdateSpecialtiesInput specialtiesDto);

   UpdateSpecialtiesOutput specialtiesEntityToUpdateSpecialtiesOutput(SpecialtiesEntity entity);

   FindSpecialtiesByIdOutput specialtiesEntityToFindSpecialtiesByIdOutput(SpecialtiesEntity entity);


}

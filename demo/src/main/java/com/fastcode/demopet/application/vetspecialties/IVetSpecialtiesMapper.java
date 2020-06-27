package com.fastcode.demopet.application.vetspecialties;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.SpecialtiesEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.application.vetspecialties.dto.*;
import com.fastcode.demopet.domain.model.VetSpecialtiesEntity;

@Mapper(componentModel = "spring")
public interface IVetSpecialtiesMapper {

   VetSpecialtiesEntity createVetSpecialtiesInputToVetSpecialtiesEntity(CreateVetSpecialtiesInput vetspecialtiesDto);
   
   @Mappings({ 
   @Mapping(source = "specialties.name", target = "specialtiesDescriptiveField"),                    
   @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField"),                    
   }) 
   CreateVetSpecialtiesOutput vetSpecialtiesEntityToCreateVetSpecialtiesOutput(VetSpecialtiesEntity entity);

	
    VetSpecialtiesEntity updateVetSpecialtiesInputToVetSpecialtiesEntity(UpdateVetSpecialtiesInput vetSpecialtiesDto);

    @Mappings({ 
    @Mapping(source = "specialties.name", target = "specialtiesDescriptiveField"),                    
    @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField"),                    
   }) 
   UpdateVetSpecialtiesOutput vetSpecialtiesEntityToUpdateVetSpecialtiesOutput(VetSpecialtiesEntity entity);

   @Mappings({ 
   @Mapping(source = "specialties.name", target = "specialtiesDescriptiveField"),                    
   @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField"),
   
   }) 
   FindVetSpecialtiesByIdOutput vetSpecialtiesEntityToFindVetSpecialtiesByIdOutput(VetSpecialtiesEntity entity);


   @Mappings({
   @Mapping(source = "vetSpecialties.specialtyId", target = "vetSpecialtiesSpecialtyId"),
   @Mapping(source = "vetSpecialties.vetId", target = "vetSpecialtiesVetId"),
   })
   GetSpecialtiesOutput specialtiesEntityToGetSpecialtiesOutput(SpecialtiesEntity specialties, VetSpecialtiesEntity vetSpecialties);

   @Mappings({
   @Mapping(source = "vetSpecialties.specialtyId", target = "vetSpecialtiesSpecialtyId"),
   @Mapping(source = "vetSpecialties.vetId", target = "vetSpecialtiesVetId"),
   })
   GetVetsOutput vetsEntityToGetVetsOutput(VetsEntity vets, VetSpecialtiesEntity vetSpecialties);

}

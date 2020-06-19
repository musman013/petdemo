package com.fastcode.demo.application.visits;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demo.domain.model.PetsEntity;
import com.fastcode.demo.domain.model.VetsEntity;
import com.fastcode.demo.application.visits.dto.*;
import com.fastcode.demo.domain.model.VisitsEntity;

@Mapper(componentModel = "spring")
public interface IVisitsMapper {

   VisitsEntity createVisitsInputToVisitsEntity(CreateVisitsInput visitsDto);
   
   @Mappings({ 
   @Mapping(source = "pets.id", target = "petId"),                   
   @Mapping(source = "pets.name", target = "petsDescriptiveField"),                    
   @Mapping(source = "vets.id", target = "vetId"),                   
   @Mapping(source = "vets.id", target = "vetsDescriptiveField"),                    
   }) 
   CreateVisitsOutput visitsEntityToCreateVisitsOutput(VisitsEntity entity);

	
    VisitsEntity updateVisitsInputToVisitsEntity(UpdateVisitsInput visitsDto);

    @Mappings({ 
    @Mapping(source = "pets.id", target = "petId"),                   
    @Mapping(source = "pets.name", target = "petsDescriptiveField"),                    
    @Mapping(source = "vets.id", target = "vetId"),                   
    @Mapping(source = "vets.id", target = "vetsDescriptiveField"),                    
   }) 
   UpdateVisitsOutput visitsEntityToUpdateVisitsOutput(VisitsEntity entity);

   @Mappings({ 
   @Mapping(source = "pets.id", target = "petId"),                   
   @Mapping(source = "pets.name", target = "petsDescriptiveField"),                    
   @Mapping(source = "vets.id", target = "vetId"),                   
   @Mapping(source = "vets.id", target = "vetsDescriptiveField"),                    
   }) 
   FindVisitsByIdOutput visitsEntityToFindVisitsByIdOutput(VisitsEntity entity);


   @Mappings({
   @Mapping(source = "pets.id", target = "id"),                  
   @Mapping(source = "visits.id", target = "visitsId"),
   })
   GetPetsOutput petsEntityToGetPetsOutput(PetsEntity pets, VisitsEntity visits);

   @Mappings({
   @Mapping(source = "vets.id", target = "id"),                  
   @Mapping(source = "visits.id", target = "visitsId"),
   })
   GetVetsOutput vetsEntityToGetVetsOutput(VetsEntity vets, VisitsEntity visits);

}

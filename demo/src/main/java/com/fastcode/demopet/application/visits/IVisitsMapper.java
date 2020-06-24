package com.fastcode.demopet.application.visits;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.application.visits.dto.*;
import com.fastcode.demopet.domain.model.VisitsEntity;

@Mapper(componentModel = "spring")
public interface IVisitsMapper {

   VisitsEntity createVisitsInputToVisitsEntity(CreateVisitsInput visitsDto);
   
   @Mappings({ 
   @Mapping(source = "pets.id", target = "petId"),                   
   @Mapping(source = "pets.name", target = "petsDescriptiveField"),   
   @Mapping(source = "vets.id", target = "vetId"),                   
   @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField") 
   }) 
   CreateVisitsOutput visitsEntityToCreateVisitsOutput(VisitsEntity entity);

	
    VisitsEntity updateVisitsInputToVisitsEntity(UpdateVisitsInput visitsDto);

    @Mappings({ 
    @Mapping(source = "pets.id", target = "petId"),                   
    @Mapping(source = "pets.name", target = "petsDescriptiveField"),    
    @Mapping(source = "vets.id", target = "vetId"),                   
    @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField")    
   }) 
   UpdateVisitsOutput visitsEntityToUpdateVisitsOutput(VisitsEntity entity);

   @Mappings({ 
   @Mapping(source = "pets.id", target = "petId"),                   
   @Mapping(source = "pets.name", target = "petsDescriptiveField"),    
   @Mapping(source = "vets.id", target = "vetId"),                   
   @Mapping(source = "vets.user.userName", target = "vetsDescriptiveField")    
   }) 
   FindVisitsByIdOutput visitsEntityToFindVisitsByIdOutput(VisitsEntity entity);


   @Mappings({
   @Mapping(source = "pets.id", target = "id"),                  
   @Mapping(source = "visits.id", target = "visitsId"),
   @Mapping(source = "visits.vets.id", target = "vetId")
   })
   GetPetsOutput petsEntityToGetPetsOutput(PetsEntity pets, VisitsEntity visits);

}

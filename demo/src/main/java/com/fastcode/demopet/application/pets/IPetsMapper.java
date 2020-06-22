package com.fastcode.demopet.application.pets;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.application.pets.dto.*;
import com.fastcode.demopet.domain.model.PetsEntity;

@Mapper(componentModel = "spring")
public interface IPetsMapper {

   PetsEntity createPetsInputToPetsEntity(CreatePetsInput petsDto);
   
   @Mappings({ 
   @Mapping(source = "types.id", target = "typeId"),                   
   @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
   @Mapping(source = "owners.user.id", target = "ownerId"),                   
   @Mapping(source = "owners.user.firstName", target = "ownersDescriptiveField"),                    
   }) 
   CreatePetsOutput petsEntityToCreatePetsOutput(PetsEntity entity);

	
    PetsEntity updatePetsInputToPetsEntity(UpdatePetsInput petsDto);

    @Mappings({ 
    @Mapping(source = "types.id", target = "typeId"),                   
    @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
    @Mapping(source = "owners.user.id", target = "ownerId"),                   
    @Mapping(source = "owners.user.firstName", target = "ownersDescriptiveField"),                    
   }) 
   UpdatePetsOutput petsEntityToUpdatePetsOutput(PetsEntity entity);

   @Mappings({ 
   @Mapping(source = "types.id", target = "typeId"),                   
   @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
   @Mapping(source = "owners.user.id", target = "ownerId"),                   
   @Mapping(source = "owners.user.firstName", target = "ownersDescriptiveField"),                    
   }) 
   FindPetsByIdOutput petsEntityToFindPetsByIdOutput(PetsEntity entity);


   @Mappings({
   @Mapping(source = "types.id", target = "id"),                  
   @Mapping(source = "types.name", target = "name"),                  
   @Mapping(source = "pets.id", target = "petsId"),
   })
   GetTypesOutput typesEntityToGetTypesOutput(TypesEntity types, PetsEntity pets);

   @Mappings({
   @Mapping(source = "owners.user.id", target = "id"),                  
   @Mapping(source = "pets.id", target = "petsId"),
   })
   GetOwnersOutput ownersEntityToGetOwnersOutput(OwnersEntity owners, PetsEntity pets);

}

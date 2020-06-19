package com.fastcode.demo.application.pets;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demo.domain.model.TypesEntity;
import com.fastcode.demo.domain.model.OwnersEntity;
import com.fastcode.demo.application.pets.dto.*;
import com.fastcode.demo.domain.model.PetsEntity;

@Mapper(componentModel = "spring")
public interface IPetsMapper {

   PetsEntity createPetsInputToPetsEntity(CreatePetsInput petsDto);
   
   @Mappings({ 
   @Mapping(source = "types.id", target = "typeId"),                   
   @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
   @Mapping(source = "owners.id", target = "ownerId"),                   
   @Mapping(source = "owners.id", target = "ownersDescriptiveField"),                    
   }) 
   CreatePetsOutput petsEntityToCreatePetsOutput(PetsEntity entity);

	
    PetsEntity updatePetsInputToPetsEntity(UpdatePetsInput petsDto);

    @Mappings({ 
    @Mapping(source = "types.id", target = "typeId"),                   
    @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
    @Mapping(source = "owners.id", target = "ownerId"),                   
    @Mapping(source = "owners.id", target = "ownersDescriptiveField"),                    
   }) 
   UpdatePetsOutput petsEntityToUpdatePetsOutput(PetsEntity entity);

   @Mappings({ 
   @Mapping(source = "types.id", target = "typeId"),                   
   @Mapping(source = "types.name", target = "typesDescriptiveField"),                    
   @Mapping(source = "owners.id", target = "ownerId"),                   
   @Mapping(source = "owners.id", target = "ownersDescriptiveField"),                    
   }) 
   FindPetsByIdOutput petsEntityToFindPetsByIdOutput(PetsEntity entity);


   @Mappings({
   @Mapping(source = "types.id", target = "id"),                  
   @Mapping(source = "types.name", target = "name"),                  
   @Mapping(source = "pets.id", target = "petsId"),
   })
   GetTypesOutput typesEntityToGetTypesOutput(TypesEntity types, PetsEntity pets);

   @Mappings({
   @Mapping(source = "owners.id", target = "id"),                  
   @Mapping(source = "pets.id", target = "petsId"),
   })
   GetOwnersOutput ownersEntityToGetOwnersOutput(OwnersEntity owners, PetsEntity pets);

}

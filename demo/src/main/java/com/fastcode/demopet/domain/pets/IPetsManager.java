package com.fastcode.demopet.domain.pets;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.domain.model.OwnersEntity;

public interface IPetsManager {
    // CRUD Operations
    PetsEntity create(PetsEntity pets);

    void delete(PetsEntity pets);

    PetsEntity update(PetsEntity pets);

    PetsEntity findById(Long id);
	
    Page<PetsEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Types
    public TypesEntity getTypes(Long petsId);
   
    //Owners
    public OwnersEntity getOwners(Long petsId);
}

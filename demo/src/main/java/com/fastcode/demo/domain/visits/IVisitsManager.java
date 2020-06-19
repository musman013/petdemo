package com.fastcode.demo.domain.visits;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demo.domain.model.VisitsEntity;
import com.fastcode.demo.domain.model.PetsEntity;
import com.fastcode.demo.domain.model.VetsEntity;

public interface IVisitsManager {
    // CRUD Operations
    VisitsEntity create(VisitsEntity visits);

    void delete(VisitsEntity visits);

    VisitsEntity update(VisitsEntity visits);

    VisitsEntity findById(Integer id);
	
    Page<VisitsEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Pets
    public PetsEntity getPets(Integer visitsId);
   
    //Vets
    public VetsEntity getVets(Integer visitsId);
}

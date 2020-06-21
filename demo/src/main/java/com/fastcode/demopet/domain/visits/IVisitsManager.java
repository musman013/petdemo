package com.fastcode.demopet.domain.visits;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.model.PetsEntity;

public interface IVisitsManager {
    // CRUD Operations
    VisitsEntity create(VisitsEntity visits);

    void delete(VisitsEntity visits);

    VisitsEntity update(VisitsEntity visits);

    VisitsEntity findById(Long id);
	
    Page<VisitsEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Pets
    public PetsEntity getPets(Long visitsId);
}

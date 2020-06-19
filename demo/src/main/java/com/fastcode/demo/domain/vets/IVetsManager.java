package com.fastcode.demo.domain.vets;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demo.domain.model.VetsEntity;
import com.fastcode.demo.domain.model.VetSpecialtiesEntity;
import com.fastcode.demo.domain.model.VisitsEntity;

public interface IVetsManager {
    // CRUD Operations
    VetsEntity create(VetsEntity vets);

    void delete(VetsEntity vets);

    VetsEntity update(VetsEntity vets);

    VetsEntity findById(Integer id);
	
    Page<VetsEntity> findAll(Predicate predicate, Pageable pageable);
}

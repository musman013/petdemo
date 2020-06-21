package com.fastcode.demopet.domain.specialties;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demopet.domain.model.SpecialtiesEntity;
import com.fastcode.demopet.domain.model.VetSpecialtiesEntity;

public interface ISpecialtiesManager {
    // CRUD Operations
    SpecialtiesEntity create(SpecialtiesEntity specialties);

    void delete(SpecialtiesEntity specialties);

    SpecialtiesEntity update(SpecialtiesEntity specialties);

    SpecialtiesEntity findById(Long id);
	
    Page<SpecialtiesEntity> findAll(Predicate predicate, Pageable pageable);
}

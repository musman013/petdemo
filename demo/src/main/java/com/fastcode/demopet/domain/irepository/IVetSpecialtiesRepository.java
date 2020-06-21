package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.model.VetSpecialtiesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.fastcode.demopet.domain.model.VetSpecialtiesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IVetSpecialtiesRepository extends JpaRepository<VetSpecialtiesEntity, VetSpecialtiesId>,QuerydslPredicateExecutor<VetSpecialtiesEntity> {

}

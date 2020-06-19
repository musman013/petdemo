package com.fastcode.demo.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demo.domain.model.VetSpecialtiesEntity; 
import com.fastcode.demo.domain.model.VisitsEntity; 
import java.util.List;
import com.fastcode.demo.domain.model.VetsEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IVetsRepository extends JpaRepository<VetsEntity, Integer>,QuerydslPredicateExecutor<VetsEntity> {

}

package com.fastcode.demo.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demo.domain.model.VetSpecialtiesEntity; 
import java.util.List;
import com.fastcode.demo.domain.model.SpecialtiesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface ISpecialtiesRepository extends JpaRepository<SpecialtiesEntity, Integer>,QuerydslPredicateExecutor<SpecialtiesEntity> {

}

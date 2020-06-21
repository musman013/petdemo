package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.domain.model.PetsEntity; 
import java.util.List;
import com.fastcode.demopet.domain.model.TypesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface ITypesRepository extends JpaRepository<TypesEntity, Long>,QuerydslPredicateExecutor<TypesEntity> {

}

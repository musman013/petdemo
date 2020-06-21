package com.fastcode.demopet.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.emailbuilder.domain.model.EmailVariableEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IEmailVariableRepository extends JpaRepository<EmailVariableEntity, Long>, QuerydslPredicateExecutor<EmailVariableEntity> {

	   @Query("select e from EmailVariableEntity e where e.id = ?1")
	   EmailVariableEntity findById(long id);

	   @Query("select e from EmailVariableEntity e where e.propertyName = ?1")
	   EmailVariableEntity findByEmailName(String value);
	
}
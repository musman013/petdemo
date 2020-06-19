package com.fastcode.demo.emailbuilder.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demo.emailbuilder.domain.model.EmailTemplateEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IEmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long>, QuerydslPredicateExecutor<EmailTemplateEntity> {

	   @Query("select e from EmailTemplateEntity e where e.id = ?1")
	   EmailTemplateEntity findById(long id);

	   @Query("select e from EmailTemplateEntity e where e.templateName = ?1")
	   EmailTemplateEntity findByEmailName(String value);
	
}

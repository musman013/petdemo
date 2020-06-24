package com.fastcode.demopet.emailbuilder.domain.irepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.emailbuilder.domain.model.EmailTemplateEntity;

@Repository
public interface IEmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long>, QuerydslPredicateExecutor<EmailTemplateEntity> {

	   @Query("select e from EmailTemplateEntity e where e.id = ?1")
	   EmailTemplateEntity findById(long id);

	   @Query("select e from EmailTemplateEntity e where e.templateName = ?1")
	   EmailTemplateEntity findByEmailName(String value);
	   
	   @Query("select distinct e.category from EmailTemplateEntity e")
	   List<String> findAllDistinctCategories();
	
}

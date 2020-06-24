package com.fastcode.demopet.emailbuilder.domain.irepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.fastcode.demopet.emailbuilder.domain.model.EmailtemplateEntityHistory;

public interface IEmailTemplateRepositoryHistory extends JpaRepository<EmailtemplateEntityHistory, Long>, QuerydslPredicateExecutor<EmailtemplateEntityHistory> {


	   @Query("select e from EmailtemplateEntityHistory e where e.id = ?1")
	   EmailtemplateEntityHistory findById(long id);

	   @Query("select e from EmailtemplateEntityHistory e where e.templateName = ?1")
	   EmailtemplateEntityHistory findByEmailName(String value);
	   
	   @Query("select distinct e.category from EmailtemplateEntityHistory e")
	   List<String> findAllDistinctCategories();
}

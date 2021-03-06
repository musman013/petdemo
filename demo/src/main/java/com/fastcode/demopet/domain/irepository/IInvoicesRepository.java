package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IInvoicesRepository extends JpaRepository<InvoicesEntity, Long>,QuerydslPredicateExecutor<InvoicesEntity> {

	@Query("select u from InvoicesEntity u where u.processInstanceId = ?1")
	InvoicesEntity findByProcessInstanceId(String value);
}

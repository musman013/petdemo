package com.fastcode.demo.scheduler.domain.irepository;

import com.fastcode.demo.scheduler.domain.model.JobDetailsEntity;
import com.fastcode.demo.scheduler.domain.model.TriggerDetailsEntity;
import com.querydsl.core.types.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ITriggerDetailsRepository extends JpaRepository<TriggerDetailsEntity, Long> , QuerydslPredicateExecutor<TriggerDetailsEntity> {

//	 @Query("select j from TriggerDetailsEntity j")
 //   Page<TriggerDetailsEntity> findAll(Pageable pageable);
}


package com.fastcode.demo.scheduler.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demo.scheduler.domain.model.JobDetailsEntity;

@Repository
public interface IJobDetailsRepository extends JpaRepository<JobDetailsEntity, Long>, QuerydslPredicateExecutor<JobDetailsEntity>  {

   // @Query("select jobDetailsEntity from JobDetailsEntity jobDetailsEntity where jobDetailsEntity =predicate")
  //  Page<JobDetailsEntity> findAll(Predicate predicate,Pageable pageable);

}

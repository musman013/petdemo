package com.fastcode.demopet.domain.irepository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;

//@JaversSpringDataAuditable
@Repository
public interface IReportversionRepository extends JpaRepository<ReportversionEntity, ReportversionId>,QuerydslPredicateExecutor<ReportversionEntity> {

	@Query("select r from ReportversionEntity r where r.user.id = ?1 and r.reportVersion = ?2")
	ReportversionEntity findByReportversionAndUserId(Long userId, String version);
	
	@Query("select r from ReportversionEntity r where r.user.id = ?1 and r.report.id = ?2 and r.reportVersion = ?3")
	ReportversionEntity findByReportIdAndVersionAndUserId(Long userId,Long reportId, String version);
	
	@Query("select r from ReportversionEntity r where r.user.id = ?1")
	List<ReportversionEntity> findByUserId(Long userId);
	
	
	
}

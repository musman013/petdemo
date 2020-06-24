package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.ReportEntity;
import java.util.List;

//@JaversSpringDataAuditable
@Repository
public interface IReportRepository extends JpaRepository<ReportEntity, Long> ,
QuerydslPredicateExecutor<ReportEntity> , IReportRepositoryCustom {

	@Query("select r from ReportEntity r where r.id = ?1 and r.user.id = ?2")
	ReportEntity findByReportIdAndUserId(Long reportId, Long userId);
	
	@Query("select r from ReportEntity r where r.user.id = ?1")
	List<ReportEntity> findByUserId(Long userId);
	
//	@Query(nativeQuery=true, value = "SELECT rv.*, (CASE WHEN rv.user_id IN (Select owner_id from report where owner_id =rv.user_id and id = rv.report_id) THEN 1 ELSE 0 END) AS shared_with_others," + 
//			"(CASE WHEN rv.user_id IN (Select user_id from reportuser where user_id =rv.user_id and report_id = rv.report_id) THEN 1 ELSE 0 END) AS shared_with_me" + 
//			"FROM reporting.reportversion rv where rv.user_id = ?1 ")
//	@Query("select r from ReportversionEntity r where r.user.id = ?1")
//	List<IReportDetailsOutput> findAllReports(Long userId);
	
}

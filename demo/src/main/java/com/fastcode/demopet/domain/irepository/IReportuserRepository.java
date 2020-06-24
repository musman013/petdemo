package com.fastcode.demopet.domain.irepository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportuserId;

//@JaversSpringDataAuditable
@Repository
public interface IReportuserRepository extends JpaRepository<ReportuserEntity, ReportuserId>,
QuerydslPredicateExecutor<ReportuserEntity> ,IReportuserRepositoryCustom {

	
	@Query("select r from ReportuserEntity r where r.user.id = ?1")
	List<ReportuserEntity> findByUserId(Long id);
	
	@Query("select r from ReportuserEntity r where r.report.id = ?1")
	List<ReportuserEntity> findByReportId(Long id);
	
	@Transactional
	@Modifying
    @Query("UPDATE ReportuserEntity r SET r.isRefreshed = ?1 WHERE r.reportId = ?2")
	List<ReportuserEntity> updateRefreshFlag(Boolean refresh,Long reportId);
	
	@Transactional
	@Modifying
    @Query("UPDATE ReportuserEntity r SET r.ownerSharingStatus = ?1 WHERE r.reportId = ?2")
	List<ReportuserEntity> updateOwnerSharingStatus(Boolean status, Long reportId);
}

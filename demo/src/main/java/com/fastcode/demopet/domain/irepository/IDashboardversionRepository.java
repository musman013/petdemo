package com.fastcode.demopet.domain.irepository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionId;

//@JaversSpringDataAuditable
@Repository
public interface IDashboardversionRepository extends JpaRepository<DashboardversionEntity, DashboardversionId>,QuerydslPredicateExecutor<DashboardversionEntity> {

	@Query("select r from DashboardversionEntity r where r.id = ?1 and r.user.id = ?2")
	DashboardversionEntity findByDashboardversionIdAndUserId(Long dashboardversionId, Long userId);
	
//	@Query("select r from DashboardversionEntity r where r.user.id = ?1 and r.version = ?2")
//	DashboardversionEntity findByDashboardversionAndUserId(Long userId, String version);
//	
//	@Query("select r from ReportversionEntity r where r.user.id = ?1 and r.dashboard.id = ?2 and r.version = ?3")
//	DashboardversionEntity findByDashboardIdAndVersionAndUserId(Long userId,Long dashboardId, String version);
//	
	@Query("select r from DashboardversionEntity r where r.user.id = ?1")
	List<DashboardversionEntity> findByUserId(Long userId);
}

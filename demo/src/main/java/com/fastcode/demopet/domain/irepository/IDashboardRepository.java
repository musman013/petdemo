package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.DashboardEntity;

import java.util.List;
import org.javers.spring.annotation.JaversSpringDataAuditable;

//@JaversSpringDataAuditable
@Repository
public interface IDashboardRepository extends JpaRepository<DashboardEntity, Long>,
        QuerydslPredicateExecutor<DashboardEntity>, IDashboardRepositoryCustom {

	@Query("select r from DashboardEntity r where r.id = ?1 and r.user.id = ?2")
	DashboardEntity findByDashboardIdAndUserId(Long dashboardId, Long userId);
	
	@Query("select r from DashboardEntity r where r.user.id = ?1")
	List<DashboardEntity> findByUserId(Long userId);
}

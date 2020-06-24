package com.fastcode.demopet.domain.irepository;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.ReportroleEntity;
import com.fastcode.demopet.domain.model.ReportroleId;

//@JaversSpringDataAuditable
@Repository
public interface IReportroleRepository extends JpaRepository<ReportroleEntity, ReportroleId>,
				QuerydslPredicateExecutor<ReportroleEntity>, IReportroleRepositoryCustom {


	@Query("select r from ReportroleEntity r where r.role.id = ?1")
	List<ReportroleEntity> findByRoleId(Long id);
}

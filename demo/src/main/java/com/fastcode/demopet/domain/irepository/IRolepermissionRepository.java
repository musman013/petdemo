package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.model.RolepermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.domain.model.RolepermissionEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IRolepermissionRepository extends JpaRepository<RolepermissionEntity, RolepermissionId>,QuerydslPredicateExecutor<RolepermissionEntity> {

	   
}

package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.processmanagement.groups.ActIdGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "actIdGroup", path = "actIdGroup")
public interface IActIdGroupRepository extends JpaRepository<ActIdGroupEntity, String>, QuerydslPredicateExecutor<ActIdGroupEntity> {

    @Query("select g from ActIdGroupEntity g where g.id = ?1")
    ActIdGroupEntity findByGroupId(String name);
    }
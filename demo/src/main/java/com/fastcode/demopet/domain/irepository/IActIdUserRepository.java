package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;

import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "actIdUser", path = "actIdUser")
public interface IActIdUserRepository extends JpaRepository<ActIdUserEntity, String>, QuerydslPredicateExecutor<ActIdUserEntity> {
    
    @Query("select u from ActIdUserEntity u where u.id = ?1")
    ActIdUserEntity findByUserId(String id);

    @Query("select u from ActIdUserEntity u where u.email = ?1")
    ActIdUserEntity findByUserEmail(String value);

}
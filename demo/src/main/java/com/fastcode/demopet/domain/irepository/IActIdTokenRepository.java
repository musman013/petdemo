package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fastcode.demopet.domain.processmanagement.tokens.ActIdTokenEntity;

import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "tokens", path = "tokens")
public interface IActIdTokenRepository extends JpaRepository<ActIdTokenEntity, String>, QuerydslPredicateExecutor<ActIdTokenEntity> {

}

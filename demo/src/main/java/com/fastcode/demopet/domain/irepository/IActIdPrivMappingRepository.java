package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.processmanagement.privilegemappings.ActIdPrivMappingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable

@RepositoryRestResource(collectionResourceRel = "privMappings", path = "privMappings")
public interface IActIdPrivMappingRepository extends JpaRepository<ActIdPrivMappingEntity, String>, QuerydslPredicateExecutor<ActIdPrivMappingEntity> {

    @Query("select pm from ActIdPrivMappingEntity pm inner join pm.actIdPriv p where pm.userId = ?1 and p.name = ?2")
    ActIdPrivMappingEntity findByUserPrivilege(String userId, String privName);

    @Query("select pm from ActIdPrivMappingEntity pm inner join pm.actIdPriv p where pm.groupId = ?1 and p.name = ?2")
    ActIdPrivMappingEntity findByGroupPrivilege(String groupId, String privName);

    @Query("select pm from ActIdPrivMappingEntity pm inner join pm.actIdPriv p where pm.userId = ?1")
    List<ActIdPrivMappingEntity> findAllByUserId(String userId);

        @Query("select pm from ActIdPrivMappingEntity pm inner join pm.actIdPriv p where pm.groupId = ?1")
        List<ActIdPrivMappingEntity> findAllByGroupId(String groupId);

            }

package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.processmanagement.memberships.ActIdMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "memberships", path = "memberships")
public interface IActIdMembershipRepository extends JpaRepository<ActIdMembershipEntity, String>, QuerydslPredicateExecutor<ActIdMembershipEntity> {

    @Query("select m from ActIdMembershipEntity m where m.userId = ?1")
    List<ActIdMembershipEntity> findAllByUserId(String id);
        
    @Query("select m from ActIdMembershipEntity m where m.userId = ?1 and m.groupId = ?2")
    ActIdMembershipEntity findByUserGroupId(String userId, String groupId);
    
    @Query("select m from ActIdMembershipEntity m where m.groupId = ?1")
    List<ActIdMembershipEntity> findAllByGroupId(String groupId);
            
}

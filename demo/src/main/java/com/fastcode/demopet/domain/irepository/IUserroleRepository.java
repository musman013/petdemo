package com.fastcode.demopet.domain.irepository;

import com.fastcode.demopet.domain.model.UserroleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.fastcode.demopet.domain.model.UserroleEntity;
import org.javers.spring.annotation.JaversSpringDataAuditable;

@JaversSpringDataAuditable
@Repository
public interface IUserroleRepository extends JpaRepository<UserroleEntity, UserroleId>,QuerydslPredicateExecutor<UserroleEntity> {
//    @Query("select e from UserroleEntity e where e.roleId = ?1 and e.userId = ?2")
//	UserroleEntity findById(Long roleId,Long userId);

}

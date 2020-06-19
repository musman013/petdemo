package com.fastcode.demo.domain.authorization.role;

import com.fastcode.demo.domain.model.RoleEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.fastcode.demo.commons.search.SearchFields;

import javax.validation.constraints.Positive;

public interface IRoleManager {

    // CRUD Operations
    RoleEntity create(RoleEntity role);

    void delete(RoleEntity role);

    RoleEntity update(RoleEntity role);

    RoleEntity findById(@Positive(message ="Id should be a positive value") Long roleId);

    //Internal operation
    RoleEntity findByRoleName(String roleName);
    
    Page<RoleEntity> findAll(Predicate predicate, Pageable pageable);

}


package com.fastcode.demopet.domain.authorization.userrole;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.domain.model.UserroleId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.RoleEntity;

public interface IUserroleManager {

    // CRUD Operations
    UserroleEntity create(UserroleEntity userrole);

    void delete(UserroleEntity userrole);

    UserroleEntity update(UserroleEntity userrole);

    UserroleEntity findById(UserroleId userroleId);
	
    Page<UserroleEntity> findAll(Predicate predicate, Pageable pageable);
   
    //User
    public UserEntity getUser(UserroleId userroleId);
   
    //Role
    public RoleEntity getRole(UserroleId userroleId);
}

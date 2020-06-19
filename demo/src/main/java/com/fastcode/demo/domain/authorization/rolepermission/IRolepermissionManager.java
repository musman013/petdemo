package com.fastcode.demo.domain.authorization.rolepermission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.fastcode.demo.domain.model.RolepermissionEntity;
import com.fastcode.demo.domain.model.RolepermissionId;
import com.fastcode.demo.domain.model.PermissionEntity;
import com.fastcode.demo.domain.model.RoleEntity;

public interface IRolepermissionManager {

    // CRUD Operations
    RolepermissionEntity create(RolepermissionEntity rolepermission);

    void delete(RolepermissionEntity rolepermission);

    RolepermissionEntity update(RolepermissionEntity rolepermission);

    RolepermissionEntity findById(RolepermissionId rolepermissionId );

    Page<RolepermissionEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Permission
    public PermissionEntity getPermission(RolepermissionId rolepermissionId );
  
    //Role
    public RoleEntity getRole(RolepermissionId rolepermissionId );
  
}

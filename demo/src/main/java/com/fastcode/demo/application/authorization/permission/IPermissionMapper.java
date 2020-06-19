package com.fastcode.demo.application.authorization.permission;

import com.fastcode.demo.application.authorization.permission.dto.*;
import com.fastcode.demo.domain.model.PermissionEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IPermissionMapper {

     PermissionEntity createPermissionInputToPermissionEntity(CreatePermissionInput permissionDto);
   
     CreatePermissionOutput permissionEntityToCreatePermissionOutput(PermissionEntity entity);

     PermissionEntity updatePermissionInputToPermissionEntity(UpdatePermissionInput permissionDto);

     UpdatePermissionOutput permissionEntityToUpdatePermissionOutput(PermissionEntity entity);

     FindPermissionByIdOutput permissionEntityToFindPermissionByIdOutput(PermissionEntity entity);

     FindPermissionByNameOutput permissionEntityToFindPermissionByNameOutput(PermissionEntity entity);
   
}
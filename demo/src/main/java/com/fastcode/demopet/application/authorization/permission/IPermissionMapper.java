package com.fastcode.demopet.application.authorization.permission;

import com.fastcode.demopet.application.authorization.permission.dto.*;
import com.fastcode.demopet.domain.model.PermissionEntity;

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
package com.fastcode.demo.application.authorization.role;

import com.fastcode.demo.application.authorization.role.dto.*;
import com.fastcode.demo.domain.model.PermissionEntity;
import com.fastcode.demo.domain.model.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IRoleMapper {

    RoleEntity createRoleInputToRoleEntity(CreateRoleInput roleDto);

    CreateRoleOutput roleEntityToCreateRoleOutput(RoleEntity entity);

    RoleEntity updateRoleInputToRoleEntity(UpdateRoleInput roleDto);

    UpdateRoleOutput roleEntityToUpdateRoleOutput(RoleEntity entity);

    FindRoleByIdOutput roleEntityToFindRoleByIdOutput(RoleEntity entity);
    
    FindRoleByNameOutput roleEntityToFindRoleByNameOutput(RoleEntity entity);

}

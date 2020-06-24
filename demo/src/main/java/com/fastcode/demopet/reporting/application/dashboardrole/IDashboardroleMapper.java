package com.fastcode.demopet.reporting.application.dashboardrole;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboardroleEntity;
import com.fastcode.demopet.reporting.application.dashboardrole.dto.*;

@Mapper(componentModel = "spring")
public interface IDashboardroleMapper {

	DashboardroleEntity createDashboardroleInputToDashboardroleEntity(CreateDashboardroleInput dashboardroleDto);

	@Mappings({ 
		@Mapping(source = "roleId", target = "roleId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "role.name", target = "roleDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	CreateDashboardroleOutput dashboardroleEntityToCreateDashboardroleOutput(DashboardroleEntity entity);

	DashboardroleEntity updateDashboardroleInputToDashboardroleEntity(UpdateDashboardroleInput dashboardroleDto);

	@Mappings({ 
		@Mapping(source = "roleId", target = "roleId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "role.name", target = "roleDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	UpdateDashboardroleOutput dashboardroleEntityToUpdateDashboardroleOutput(DashboardroleEntity entity);

	@Mappings({ 
		@Mapping(source = "roleId", target = "roleId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "role.name", target = "roleDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	FindDashboardroleByIdOutput dashboardroleEntityToFindDashboardroleByIdOutput(DashboardroleEntity entity);

	@Mappings({
		@Mapping(source = "dashboardrole.roleId", target = "dashboardroleRoleId"),
	//	@Mapping(source = "dashboardrole.dashboardId", target = "dashboardroleDashboardId"),
	})
	GetDashboardOutput dashboardEntityToGetDashboardOutput(DashboardEntity dashboard, DashboardroleEntity dashboardrole);

}

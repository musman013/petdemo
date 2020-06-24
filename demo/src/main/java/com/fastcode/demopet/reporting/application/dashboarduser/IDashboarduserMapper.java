package com.fastcode.demopet.reporting.application.dashboarduser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.reporting.application.dashboarduser.dto.*;

@Mapper(componentModel = "spring")
public interface IDashboarduserMapper {

	DashboarduserEntity createDashboarduserInputToDashboarduserEntity(CreateDashboarduserInput dashboarduserDto);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	CreateDashboarduserOutput dashboarduserEntityToCreateDashboarduserOutput(DashboarduserEntity entity);

	DashboarduserEntity updateDashboarduserInputToDashboarduserEntity(UpdateDashboarduserInput dashboarduserDto);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	UpdateDashboarduserOutput dashboarduserEntityToUpdateDashboarduserOutput(DashboarduserEntity entity);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "dashboardId", target = "dashboardId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
	}) 
	FindDashboarduserByIdOutput dashboarduserEntityToFindDashboarduserByIdOutput(DashboarduserEntity entity);

	@Mappings({
		@Mapping(source = "dashboarduser.userId", target = "dashboarduserUserId"),
	//	@Mapping(source = "dashboarduser.dashboardId", target = "dashboarduserDashboardId"),
	})
	GetDashboardOutput dashboardEntityToGetDashboardOutput(DashboardEntity dashboard, DashboarduserEntity dashboarduser);

}

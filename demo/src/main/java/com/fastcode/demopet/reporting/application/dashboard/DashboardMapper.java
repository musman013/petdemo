package com.fastcode.demopet.reporting.application.dashboard;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboard.dto.*;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.CreateDashboardversionInput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.CreateDashboardversionOutput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.UpdateDashboardversionInput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.UpdateDashboardversionOutput;
import com.fastcode.demopet.reporting.application.report.dto.CreateReportInput;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

	@Mappings({ 
		@Mapping(source = "ownerId", target = "userId")                         
	}) 
	CreateDashboardversionInput creatDashboardInputToCreateDashboardversionInput(CreateDashboardInput dashboardDto);

	DashboardEntity createDashboardInputToDashboardEntity(CreateDashboardInput dashboardDto);

	CreateDashboardInput addNewReportToNewDashboardInputTocreatDashboardInput(AddNewReportToNewDashboardInput input);
	
	CreateDashboardInput addExistingReportToNewDashboardInputTocreatDashboardInput(AddExistingReportToNewDashboardInput input);
	
//	DashboardEntity createDashboardAndReportInputToDashboardEntity(AddNewReportToNewDashboardInput dashboardDto);

	DashboardEntity addExistingReportToNewDashboardInputToDashboardEntity(AddExistingReportToNewDashboardInput input);

	ReportEntity createDashboardAndReportInputToReportEntity(CreateReportInput reportDto);

	@Mappings({ 
		@Mapping(source = "entity.id", target = "id"), 
		@Mapping(source = "entity.user.id", target = "ownerId"),                   
		@Mapping(source = "entity.user.userName", target = "ownerDescriptiveField")                     
	}) 
	CreateDashboardOutput dashboardEntityAndCreateDashboardversionOutputToCreateDashboardOutput(DashboardEntity entity, CreateDashboardversionOutput dashboardversion);

	@Mappings({ 
		@Mapping(source = "dashboardversion.dashboardId", target = "id"), 
		@Mapping(source = "dashboardversion.userId", target = "ownerId"),                   
		@Mapping(source = "dashboardversion.user.userName", target = "ownerDescriptiveField")                     
	}) 
	CreateDashboardOutput dashboardEntityAndDashboardversionEntityToCreateDashboardOutput(DashboardEntity dashboard, DashboardversionEntity dashboardversion);

	
	UpdateDashboardversionInput updateDashboardInputToUpdateDashboardversionInput(UpdateDashboardInput dashboardDto);
	
	@Mappings({ 
		@Mapping(source = "entity.id", target = "id"), 
		@Mapping(source = "entity.user.id", target = "ownerId"),                   
		@Mapping(source = "entity.user.userName", target = "ownerDescriptiveField") 	
	}) 
	UpdateDashboardOutput dashboardEntityAndUpdateDashboardversionOutputToUpdateDashboardOutput(DashboardEntity entity, UpdateDashboardversionOutput dashboardversion);
	
	DashboardEntity updateDashboardInputToDashboardEntity(UpdateDashboardInput dashboardDto);

	@Mappings({ 
		@Mapping(source = "user.id", target = "ownerId")                    
	}) 
	UpdateDashboardOutput dashboardEntityToUpdateDashboardOutput(DashboardEntity entity);

	@Mappings({ 
		@Mapping(source = "user.id", target = "ownerId"),  
	//	@Mapping(source = "entity.id", target = "dashboardId"),
		@Mapping(source = "user.userName", target = "ownerDescriptiveField")                 
	}) 
	FindDashboardByIdOutput dashboardEntityToFindDashboardByIdOutput(DashboardEntity entity);
	
	@Mappings({ 
		@Mapping(source = "ownerId", target = "userId") 
		//@Mapping(source = "entity.id", target = "dashboardId")                
	}) 
	FindDashboardByIdOutput dashboardOutputToFindDashboardByIdOutput(CreateDashboardOutput entity);

	@Mappings({
		@Mapping(source = "dashboardversion.userId", target = "userId"), 
		@Mapping(source = "dashboard.user.id", target = "ownerId"),
		@Mapping(source = "dashboardversion.dashboardId", target = "id"),
		@Mapping(source = "dashboard.version", target = "version")
	})
	FindDashboardByIdOutput dashboardEntitiesToFindDashboardByIdOutput(DashboardEntity dashboard, DashboardversionEntity dashboardversion, DashboarduserEntity dashboarduser);

	@Mappings({
		@Mapping(source = "dashboardversion.userId", target = "userId"),  
		@Mapping(source = "dashboard.user.id", target = "ownerId"),
		@Mapping(source = "dashboardversion.dashboardId", target = "id"),
	})
	DashboardDetailsOutput dashboardEntitiesToDashboardDetailsOutput(DashboardEntity dashboard, DashboardversionEntity dashboardversion, DashboarduserEntity dashboarduser);

	@Mappings({
		@Mapping(source = "user.id", target = "id"),                  
		@Mapping(source = "dashboard.id", target = "dashboardId"),
	})
	GetUserOutput userEntityToGetUserOutput(UserEntity user, DashboardEntity dashboard);
	
	@Mappings({
		@Mapping(source = "role.id", target = "id"),                  
		@Mapping(source = "dashboard.id", target = "dashboardId"),
	})
	GetRoleOutput roleEntityToGetRoleOutput(RoleEntity role, DashboardEntity dashboard);

}

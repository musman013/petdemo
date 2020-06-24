package com.fastcode.demopet.reporting.application.report;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.report.dto.*;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionOutput;

@Mapper(componentModel = "spring")
public interface ReportMapper {

	ReportEntity createReportInputToReportEntity(CreateReportInput reportDto);
	
	@Mappings({ 
		@Mapping(source = "ownerId", target = "userId")                         
	}) 
	CreateReportversionInput createReportInputToCreateReportversionInput(CreateReportInput reportDto);

	@Mappings({ 
		@Mapping(source = "entity.id", target = "id"), 
		@Mapping(source = "entity.user.id", target = "ownerId"),                   
		@Mapping(source = "entity.user.userName", target = "ownerDescriptiveField")                  
	}) 
	CreateReportOutput reportEntityAndCreateReportversionOutputToCreateReportOutput(ReportEntity entity, CreateReportversionOutput reportversionOutput);

	@Mappings({ 
		@Mapping(source = "report.id", target = "id"), 
		@Mapping(source = "report.user.id", target = "ownerId"),                   
		@Mapping(source = "report.user.userName", target = "ownerDescriptiveField")                  
	}) 
	CreateReportOutput reportEntityAndReportversionEntityToCreateReportOutput(ReportEntity report, ReportversionEntity reportversion);

//	@Mappings({ 
//		@Mapping(source = "id", target = "reportId")
//	})
	FindReportByIdOutput createReportOutputToFindReportByIdOutput(CreateReportOutput report, ReportuserEntity reportuser);

	ReportEntity updateReportInputToReportEntity(UpdateReportInput reportDto);
	
	UpdateReportversionInput updateReportInputToUpdateReportversionInput(UpdateReportInput reportDto);

	@Mappings({ 
		@Mapping(source = "entity.id", target = "id"), 
		@Mapping(source = "entity.user.id", target = "ownerId"),                   
		@Mapping(source = "entity.user.userName", target = "ownerDescriptiveField") 	
	}) 
	UpdateReportOutput reportEntityAndUpdateReportversionOutputToUpdateReportOutput(ReportEntity entity, UpdateReportversionOutput reportversion);

	@Mappings({
		@Mapping(source = "reportversion.userId", target = "userId"), 
		@Mapping(source = "report.user.id", target = "ownerId"),
		@Mapping(source = "reportversion.reportId", target = "id"),
		@Mapping(source = "report.version", target = "version")
	})
	FindReportByIdOutput reportEntitiesToFindReportByIdOutput(ReportEntity report, ReportversionEntity reportversion, ReportuserEntity reportuser);

	@Mappings({ 
		//@Mapping(source = "entity.id", target = "id"),
		@Mapping(source = "entity.user.id", target = "ownerId"),
		@Mapping(source = "entity.version", target = "version")
	}) 
	FindReportByIdOutput reportEntityToFindReportByIdOutput(ReportEntity entity, ReportversionEntity reportversion);

	@Mappings({
		@Mapping(source = "user.id", target = "id"),  
		@Mapping(source = "report.id", target = "reportId"),
	})
	GetUserOutput userEntityToGetUserOutput(UserEntity user, ReportEntity report);
	
	@Mappings({
		@Mapping(source = "role.id", target = "id"),  
		@Mapping(source = "report.id", target = "reportId"),
	})
	GetRoleOutput roleEntityToGetRoleOutput(RoleEntity role, ReportEntity report);
	
	@Mappings({
		@Mapping(source = "reportversion.userId", target = "userId"),  
		@Mapping(source = "report.user.id", target = "ownerId"),
		@Mapping(source = "reportversion.reportId", target = "id")
	})
	ReportDetailsOutput reportEntitiesToReportDetailsOutput(ReportEntity report, ReportversionEntity reportversion, ReportuserEntity reportuser);
	
}

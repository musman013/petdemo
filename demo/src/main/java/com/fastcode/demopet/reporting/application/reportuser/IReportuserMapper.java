package com.fastcode.demopet.reporting.application.reportuser;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.reporting.application.reportuser.dto.*;

@Mapper(componentModel = "spring")
public interface IReportuserMapper {

	ReportuserEntity createReportuserInputToReportuserEntity(CreateReportuserInput reportuserDto);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "reportId", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "report.title", target = "reportDescriptiveField"),                    
	}) 
	CreateReportuserOutput reportuserEntityToCreateReportuserOutput(ReportuserEntity entity);

	ReportuserEntity updateReportuserInputToReportuserEntity(UpdateReportuserInput reportuserDto);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "reportId", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "report.title", target = "reportDescriptiveField"),                    
	}) 
	UpdateReportuserOutput reportuserEntityToUpdateReportuserOutput(ReportuserEntity entity);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "reportId", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	//	@Mapping(source = "report.title", target = "reportDescriptiveField"),                    
	}) 
	FindReportuserByIdOutput reportuserEntityToFindReportuserByIdOutput(ReportuserEntity entity);

	@Mappings({
		@Mapping(source = "reportuser.userId", target = "reportuserUserId"),
	//	@Mapping(source = "reportuser.reportId", target = "reportuserReportId"),
	})
	GetReportOutput reportEntityToGetReportOutput(ReportEntity report, ReportuserEntity reportuser);

}

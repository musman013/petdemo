package com.fastcode.demopet.reporting.application.reportversion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.FindReportversionByIdOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.GetUserOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionOutput;


@Mapper(componentModel = "spring")
public interface IReportversionMapper {
	ReportversionEntity createReportversionInputToReportversionEntity(CreateReportversionInput reportversionDto);

	@Mappings({ 
		@Mapping(source = "user.id", target = "userId"),
		@Mapping(source = "report.id", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField")
	}) 
	CreateReportversionOutput reportversionEntityToCreateReportversionOutput(ReportversionEntity entity);

	@Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "version", target = "reportVersion")
	}) 
	ReportversionEntity reportversionEntityToReportversionEntity(ReportversionEntity entity,Long userId, String version);

	ReportversionEntity updateReportversionInputToReportversionEntity(UpdateReportversionInput reportversionDto);

	@Mappings({  
		@Mapping(source = "user.id", target = "userId"),  
		@Mapping(source = "report.id", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	}) 
	UpdateReportversionOutput reportversionEntityToUpdateReportversionOutput(ReportversionEntity entity);

	@Mappings({ 
		@Mapping(source = "user.id", target = "userId"),          
		@Mapping(source = "report.id", target = "reportId"),
		@Mapping(source = "user.userName", target = "userDescriptiveField"),                    
	}) 
	FindReportversionByIdOutput reportversionEntityToFindReportversionByIdOutput(ReportversionEntity entity);


	@Mappings({
		@Mapping(source = "user.id", target = "id"),                  
		@Mapping(source = "reportversion.reportVersion", target = "reportVersion"),
	})
	GetUserOutput userEntityToGetUserOutput(UserEntity user, ReportversionEntity reportversion);

}
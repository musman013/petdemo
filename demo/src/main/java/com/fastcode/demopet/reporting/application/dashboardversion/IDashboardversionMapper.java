package com.fastcode.demopet.reporting.application.dashboardversion;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.*;

@Mapper(componentModel = "spring")
public interface IDashboardversionMapper {
	
   DashboardversionEntity createDashboardversionInputToDashboardversionEntity(CreateDashboardversionInput dashboardversionDto);

   @Mappings({ 
   @Mapping(source = "user.id", target = "userId"),                   
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                    
   }) 
   CreateDashboardversionOutput dashboardversionEntityToCreateDashboardversionOutput(DashboardversionEntity entity);

   DashboardversionEntity updateDashboardversionInputToDashboardversionEntity(UpdateDashboardversionInput dashboardversionDto);

   @Mappings({ 
   @Mapping(source = "user.id", target = "userId"),                   
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                    
   }) 
   UpdateDashboardversionOutput dashboardversionEntityToUpdateDashboardversionOutput(DashboardversionEntity entity);

   @Mappings({ 
   @Mapping(source = "user.id", target = "userId"),                   
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                    
   }) 
   FindDashboardversionByIdOutput dashboardversionEntityToFindDashboardversionByIdOutput(DashboardversionEntity entity);

   @Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "dversion", target = "dashboardVersion")
	}) 
	DashboardversionEntity dashboardversionEntityToDashboardversionEntity(DashboardversionEntity entity,Long userId, String dversion);

   @Mappings({ 
		@Mapping(source = "userId", target = "userId"),
		@Mapping(source = "dversion", target = "dashboardVersion")
	}) 
    DashboardversionreportEntity dashboardversionreportEntityToDashboardversionreportEntity(DashboardversionreportEntity dashboardreport,Long userId,String dversion);
   
   @Mappings({
		@Mapping(source = "user.id", target = "id"),                  
		@Mapping(source = "dashboardversion.dashboardVersion", target = "dashboardVersion"),
	})
   GetUserOutput userEntityToGetUserOutput(UserEntity user, DashboardversionEntity dashboardversion);

}

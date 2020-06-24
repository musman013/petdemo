package com.fastcode.demopet.reporting.application.dashboardversionreport;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.reporting.application.dashboardversionreport.dto.*;

@Mapper(componentModel = "spring")
public interface DashboardversionreportMapper {

   DashboardversionreportEntity createReportdashboardInputToDashboardversionreportEntity(CreateDashboardversionreportInput reportdashboardDto);
   
   @Mappings({ 
   @Mapping(source = "dashboardId", target = "dashboardId"),
   @Mapping(source = "reportId", target = "reportId"),
 //  @Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
 //  @Mapping(source = "report.title", target = "reportDescriptiveField"),                    
   }) 
   CreateDashboardversionreportOutput reportdashboardEntityToCreateReportdashboardOutput(DashboardversionreportEntity entity);

   DashboardversionreportEntity updateReportdashboardInputToDashboardversionreportEntity(UpdateDashboardversionreportInput reportdashboardDto);

   @Mappings({ 
   @Mapping(source = "dashboardId", target = "dashboardId"),
   @Mapping(source = "reportId", target = "reportId"),
//   @Mapping(source = "dashboard.title", target = "dashboardDescriptiveField"),                    
//   @Mapping(source = "report.title", target = "reportDescriptiveField"),                    
   }) 
   UpdateDashboardversionreportOutput reportdashboardEntityToUpdateReportdashboardOutput(DashboardversionreportEntity entity);


   FindDashboardversionreportByIdOutput reportdashboardEntityToFindReportdashboardByIdOutput(DashboardversionreportEntity entity);


   @Mappings({
   @Mapping(source = "reportdashboard.dashboardId", target = "reportdashboardDashboardId"),
   @Mapping(source = "reportdashboard.reportId", target = "reportdashboardReportId"),
   })
   GetDashboardversionOutput dashboardversionEntityToGetDashboardversionOutput(DashboardversionEntity dashboardversion, DashboardversionreportEntity reportdashboard);

   @Mappings({
   @Mapping(source = "reportdashboard.dashboardId", target = "reportdashboardDashboardId"),
   @Mapping(source = "reportdashboard.reportId", target = "reportdashboardReportId"),
   })
   GetReportOutput reportEntityToGetReportOutput(ReportEntity report, DashboardversionreportEntity reportdashboard);

}

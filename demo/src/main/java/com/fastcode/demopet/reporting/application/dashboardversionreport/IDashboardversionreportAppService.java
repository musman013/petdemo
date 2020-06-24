package com.fastcode.demopet.reporting.application.dashboardversionreport;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.domain.model.DashboardversionreportId;
import com.fastcode.demopet.reporting.application.dashboardversionreport.dto.*;

@Service
public interface IDashboardversionreportAppService {

	CreateDashboardversionreportOutput create(CreateDashboardversionreportInput reportdashboard);

    void delete(DashboardversionreportId reportdashboardId);

    UpdateDashboardversionreportOutput update(DashboardversionreportId reportdashboardId, UpdateDashboardversionreportInput input);

    FindDashboardversionreportByIdOutput findById(DashboardversionreportId reportdashboardId);

    List<FindDashboardversionreportByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

	public DashboardversionreportId parseReportdashboardKey(String keysString);
    
    //Dashboard
    GetDashboardversionOutput getDashboard(DashboardversionreportId reportdashboardId);
    
    //Report
    GetReportOutput getReport(DashboardversionreportId reportdashboardId);
}

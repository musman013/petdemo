package com.fastcode.demopet.reporting.domain.dashboardversionreport;

import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportId;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDashboardversionreportManager {
    // CRUD Operations
    DashboardversionreportEntity create(DashboardversionreportEntity reportdashboard);

    void delete(DashboardversionreportEntity reportdashboard);

    DashboardversionreportEntity update(DashboardversionreportEntity reportdashboard);

    DashboardversionreportEntity findById(DashboardversionreportId reportdashboardId);
    
    List<DashboardversionreportEntity> findByUserId(Long userId);
    
    List<DashboardversionreportEntity> findByIdIfUserIdIsNotSame(Long dashboardId, Long reportId, Long userId, String version);
    
    List<DashboardversionreportEntity> findByReportIdAndUserIdAndVersion(Long reportId, Long userId, String version);
    
    List<DashboardversionreportEntity> findByDashboardId(Long dashboardId);
    
    List<DashboardversionreportEntity> findByDashboardIdAndVersionAndUserId(Long dashboardId, String version, Long userId);
    
    List<DashboardversionreportEntity> findByDashboardIdAndVersionAndUserIdInDesc(Long id, String version, Long userId);
	
    Page<DashboardversionreportEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Dashboard
    public DashboardversionEntity getDashboardversion(DashboardversionreportId reportdashboardId);
   
    //Report
    public ReportEntity getReport(DashboardversionreportId reportdashboardId);
}

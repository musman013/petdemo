package com.fastcode.demopet.reporting.domain.report;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.report.dto.ReportDetailsOutput;

public interface IReportManager {
    // CRUD Operations
    ReportEntity create(ReportEntity report);

    void delete(ReportEntity report);

    ReportEntity update(ReportEntity report);

    ReportEntity findById(Long id);
	
    Page<ReportEntity> findAll(Predicate predicate, Pageable pageable);
    
    public Page<ReportDetailsOutput> getReports(Long userId,String search, Pageable pageable) throws Exception;
   
    public Page<ReportDetailsOutput> getSharedReports(Long userId,String search, Pageable pageable) throws Exception;
    
    ReportEntity findByReportIdAndUserId(Long id, Long userId);
    
    List<ReportEntity> findByUserId(Long userId);
    //User
    public UserEntity getUser(Long reportId);
}

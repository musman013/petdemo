package com.fastcode.demopet.reporting.domain.reportrole;

import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportroleEntity;
import com.fastcode.demopet.domain.model.ReportroleId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IReportroleManager {
    // CRUD Operations
    ReportroleEntity create(ReportroleEntity reportrole);

    void delete(ReportroleEntity reportrole);

    ReportroleEntity update(ReportroleEntity reportrole);

    ReportroleEntity findById(ReportroleId reportroleId);
    
    List<ReportroleEntity> findByRoleId(Long id);
    
    public Page<RoleEntity> getAvailableRolesList(Long reportId, String search, Pageable pageable);
    
    public Page<RoleEntity> getReportRolesList(Long reportId, String search, Pageable pageable);
    
	
    Page<ReportroleEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Role
    public RoleEntity getRole(ReportroleId reportroleId);
   
    //Report
    public ReportEntity getReport(ReportroleId reportroleId);
}

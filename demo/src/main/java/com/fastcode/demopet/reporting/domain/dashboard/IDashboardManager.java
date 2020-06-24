package com.fastcode.demopet.reporting.domain.dashboard;

import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboard.dto.DashboardDetailsOutput;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDashboardManager {
    // CRUD Operations
    DashboardEntity create(DashboardEntity dashboard);

    void delete(DashboardEntity dashboard);

    DashboardEntity update(DashboardEntity dashboard);

    DashboardEntity findById(Long id);
    
    DashboardEntity findByDashboardIdAndUserId(Long id, Long userId);
    
    public Page<DashboardDetailsOutput> getDashboards(Long userId,String search, Pageable pageable) throws Exception;
    
    public Page<DashboardDetailsOutput> getAvailableDashboards(Long userId,Long reportId, String search, Pageable pageable) throws Exception;
    
    public Page<DashboardDetailsOutput> getSharedDashboards(Long userId,String search, Pageable pageable) throws Exception;
    
    List<DashboardEntity> findByUserId(Long userId);
	
    Page<DashboardEntity> findAll(Predicate predicate, Pageable pageable);
   
    //User
    public UserEntity getUser(Long dashboardId);
}

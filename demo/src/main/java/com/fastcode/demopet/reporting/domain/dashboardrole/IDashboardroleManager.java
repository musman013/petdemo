package com.fastcode.demopet.reporting.domain.dashboardrole;

import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboardroleEntity;
import com.fastcode.demopet.domain.model.DashboardroleId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IDashboardroleManager {
    // CRUD Operations
    DashboardroleEntity create(DashboardroleEntity dashboardrole);

    void delete(DashboardroleEntity dashboardrole);

    DashboardroleEntity update(DashboardroleEntity dashboardrole);

    DashboardroleEntity findById(DashboardroleId dashboardroleId);
    
    List<DashboardroleEntity> findByRoleId(Long id);
    
    public Page<RoleEntity> getAvailableRolesList(Long dashboardId, String search, Pageable pageable);
    
    public Page<RoleEntity> getDashboardRolesList(Long dashboardId, String search, Pageable pageable);
    
	
    Page<DashboardroleEntity> findAll(Predicate predicate, Pageable pageable);
   
    //Role
    public RoleEntity getRole(DashboardroleId dashboardroleId);
   
    //Dashboard
    public DashboardEntity getDashboard(DashboardroleId dashboardroleId);
}

package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.domain.model.RoleEntity;

public interface IDashboardroleRepositoryCustom {

	Page<RoleEntity> getAvailableDashboardrolesList(Long dashboardId, String search, Pageable pageable);

	Page<RoleEntity> getDashboardrolesList(Long dashboardId, String search, Pageable pageable);

}

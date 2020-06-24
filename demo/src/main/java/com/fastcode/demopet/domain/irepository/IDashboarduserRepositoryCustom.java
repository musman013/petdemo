package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.domain.model.UserEntity;

public interface IDashboarduserRepositoryCustom {

	Page<UserEntity> getAvailableDashboardusersList(Long dashboardId, String search, Pageable pageable);

	Page<UserEntity> getDashboardusersList(Long dashboardId, String search, Pageable pageable);

}

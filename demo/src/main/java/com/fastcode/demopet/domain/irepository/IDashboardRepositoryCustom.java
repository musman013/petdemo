package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.reporting.application.dashboard.dto.DashboardDetailsOutput;

public interface IDashboardRepositoryCustom {
	
	Page<DashboardDetailsOutput> getAllDashboardsByUserId(Long userId, String search, Pageable pageable) throws Exception;

	Page<DashboardDetailsOutput> getSharedDashboardsByUserId(Long userId, String search, Pageable pageable) throws Exception;
	
	Page<DashboardDetailsOutput> getAvailableDashboardsByUserId(Long userId,Long reportId, String search, Pageable pageable) throws Exception;

}

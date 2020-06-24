package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.domain.model.UserEntity;

public interface IReportuserRepositoryCustom {

	Page<UserEntity> getAvailableReportusersList(Long reportId, String search, Pageable pageable);
	
	Page<UserEntity> getReportusersList(Long reportId, String search, Pageable pageable);
	
//	public List<ReportuserEntity> findByReportId(Long reportId);
	
}

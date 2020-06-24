package com.fastcode.demopet.domain.irepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fastcode.demopet.reporting.application.report.dto.ReportDetailsOutput;

public interface IReportRepositoryCustom {
	
	Page<ReportDetailsOutput> getAllReportsByUserId(Long userId, String search, Pageable pageable) throws Exception;

	Page<ReportDetailsOutput> getSharedReportsByUserId(Long userId, String search, Pageable pageable) throws Exception;
		
}

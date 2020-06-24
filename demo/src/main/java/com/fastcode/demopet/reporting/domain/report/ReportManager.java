package com.fastcode.demopet.reporting.domain.report;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.irepository.IDashboardversionreportRepository;
import com.fastcode.demopet.domain.irepository.IReportRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.report.dto.ReportDetailsOutput;
import com.querydsl.core.types.Predicate;

@Repository
public class ReportManager implements IReportManager {

    @Autowired
    IReportRepository  _reportRepository;
    
    @Autowired
	IDashboardversionreportRepository  _reportdashboardRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
	public ReportEntity create(ReportEntity report) {

		return _reportRepository.save(report);
	}

	public void delete(ReportEntity report) {

		_reportRepository.delete(report);	
	}

	public ReportEntity update(ReportEntity report) {

		return _reportRepository.save(report);
	}

	public ReportEntity findById(Long reportId) {
    	Optional<ReportEntity> dbReport= _reportRepository.findById(reportId);
		if(dbReport.isPresent()) {
			ReportEntity existingReport = dbReport.get();
		    return existingReport;
		} else {
		    return null;
		}

	}
	
	public ReportEntity findByReportIdAndUserId(Long reportId,Long userId)
	{
		return _reportRepository.findByReportIdAndUserId(reportId, userId);
	}
	
	public List<ReportEntity> findByUserId(Long userId)
	{
		return _reportRepository.findByUserId(userId);
	}

	public Page<ReportEntity> findAll(Predicate predicate, Pageable pageable) {

		return _reportRepository.findAll(predicate,pageable);
	}
	
	public Page<ReportDetailsOutput> getReports(Long userId, String search, Pageable pageable) throws Exception
	{
		Page<ReportDetailsOutput> list = _reportRepository.getAllReportsByUserId(userId, search, pageable);
		return list;
	}
	
	public Page<ReportDetailsOutput> getSharedReports(Long userId, String search, Pageable pageable) throws Exception
	{
		Page<ReportDetailsOutput> list = _reportRepository.getSharedReportsByUserId(userId, search, pageable);
		return list;
	}
   //User
	public UserEntity getUser(Long reportId) {
		
		Optional<ReportEntity> dbReport= _reportRepository.findById(reportId);
		if(dbReport.isPresent()) {
			ReportEntity existingReport = dbReport.get();
		    return existingReport.getUser();
		} else {
		    return null;
		}
	}
}

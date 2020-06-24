package com.fastcode.demopet.reporting.domain.reportuser;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.irepository.IReportRepository;
import com.fastcode.demopet.domain.irepository.IReportuserRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;

@Component
public class ReportuserManager implements IReportuserManager {

    @Autowired
    IReportuserRepository  _reportuserRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    @Autowired
	IReportRepository  _reportRepository;
    
	public ReportuserEntity create(ReportuserEntity reportuser) {

		return _reportuserRepository.save(reportuser);
	}

	public void delete(ReportuserEntity reportuser) {

		_reportuserRepository.delete(reportuser);	
	}

	public ReportuserEntity update(ReportuserEntity reportuser) {

		return _reportuserRepository.save(reportuser);
	}

	public ReportuserEntity findById(ReportuserId reportuserId) {
    	Optional<ReportuserEntity> dbReportuser= _reportuserRepository.findById(reportuserId);
		if(dbReportuser.isPresent()) {
			ReportuserEntity existingReportuser = dbReportuser.get();
		    return existingReportuser;
		} else {
		    return null;
		}

	}
	
	public Page<UserEntity> getAvailableUsersList(Long reportId, String search, Pageable pageable) {
		return _reportuserRepository.getAvailableReportusersList(reportId, search, pageable);
	}
	
	public Page<UserEntity> getReportUsersList(Long reportId, String search, Pageable pageable) {
		return _reportuserRepository.getReportusersList(reportId, search, pageable);
	}
	
	public List<ReportuserEntity> findByUserId(Long userId)
	{
		return _reportuserRepository.findByUserId(userId);
	}
	
	public List<ReportuserEntity> findByReportId(Long reportId)
	{
		return _reportuserRepository.findByReportId(reportId);
	}
	
	public List<ReportuserEntity> updateRefreshFlag(Long reportId, Boolean refresh)
	{
		return _reportuserRepository.updateRefreshFlag(refresh, reportId);
	}
	
	public List<ReportuserEntity> updateOwnerSharingStatus(Long reportId, Boolean status)
	{
		return _reportuserRepository.updateOwnerSharingStatus(status, reportId);
	}


	public Page<ReportuserEntity> findAll(Predicate predicate, Pageable pageable) {

		return _reportuserRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(ReportuserId reportuserId) {
		
		Optional<ReportuserEntity> dbReportuser= _reportuserRepository.findById(reportuserId);
		if(dbReportuser.isPresent()) {
			ReportuserEntity existingReportuser = dbReportuser.get();
		    return existingReportuser.getUser();
		} else {
		    return null;
		}
	}
  
   //Report
	public ReportEntity getReport(ReportuserId reportuserId) {
		
		Optional<ReportuserEntity> dbReportuser= _reportuserRepository.findById(reportuserId);
		if(dbReportuser.isPresent()) {
			ReportuserEntity existingReportuser = dbReportuser.get();
		    return existingReportuser.getReport();
		} else {
		    return null;
		}
	}
}

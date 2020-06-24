package com.fastcode.demopet.reporting.domain.reportversion;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.irepository.IDashboardversionreportRepository;
import com.fastcode.demopet.domain.irepository.IReportversionRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class ReportversionManager implements IReportversionManager {

    @Autowired
    IReportversionRepository  _reportversionRepository;
    
    @Autowired
	IDashboardversionreportRepository  _reportdashboardRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
	public ReportversionEntity create(ReportversionEntity report) {

		return _reportversionRepository.save(report);
	}

	public void delete(ReportversionEntity report) {

		_reportversionRepository.delete(report);	
	}

	public ReportversionEntity update(ReportversionEntity report) {

		return _reportversionRepository.save(report);
	}

	public ReportversionEntity findById(ReportversionId reportversionId) {
    	Optional<ReportversionEntity> dbReportversion= _reportversionRepository.findById(reportversionId);
		if(dbReportversion.isPresent()) {
			ReportversionEntity existingReportversion = dbReportversion.get();
		    return existingReportversion;
		} else {
		    return null;
		}

	}
	
//	public ReportversionEntity findByReportIdAndVersionAndUserId(ReportversionId reportversionId)
//	{
//		return _reportversionRepository.findByReportIdAndVersionAndUserId(reportversionId.getUserId(),reportversionId.getReportId(), reportversionId.getVersion());
//	}
	
	public List<ReportversionEntity> findByUserId(Long userId)
	{
		return _reportversionRepository.findByUserId(userId);
	}

	public Page<ReportversionEntity> findAll(Predicate predicate, Pageable pageable) {

		return _reportversionRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(ReportversionId reportId) {
		
		Optional<ReportversionEntity> dbReportversion= _reportversionRepository.findById(reportId);
		if(dbReportversion.isPresent()) {
			ReportversionEntity existingReportversion = dbReportversion.get();
		    return existingReportversion.getUser();
		} else {
		    return null;
		}
	}
}

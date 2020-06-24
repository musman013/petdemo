package com.fastcode.demopet.reporting.domain.dashboardversion;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.irepository.IDashboardversionRepository;
import com.fastcode.demopet.domain.irepository.IDashboardversionreportRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;

@Component
public class DashboardversionManager implements IDashboardversionManager {

    @Autowired
    IDashboardversionRepository  _dashboardversionRepository;
    
    @Autowired
	IDashboardversionreportRepository  _reportdashboardRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
	public DashboardversionEntity create(DashboardversionEntity dashboardversion) {

		return _dashboardversionRepository.save(dashboardversion);
	}

	public void delete(DashboardversionEntity dashboardversion) {

		_dashboardversionRepository.delete(dashboardversion);	
	}

	public DashboardversionEntity update(DashboardversionEntity dashboardversion) {

		return _dashboardversionRepository.save(dashboardversion);
	}

	public DashboardversionEntity findById(DashboardversionId dashboardversionId) {
    	Optional<DashboardversionEntity> dbDashboardversion= _dashboardversionRepository.findById(dashboardversionId);
		if(dbDashboardversion.isPresent()) {
			DashboardversionEntity existingDashboardversion = dbDashboardversion.get();
		    return existingDashboardversion;
		} else {
		    return null;
		}

	}
	
//	public DashboardversionEntity findByDashboardversionIdAndUserId(Long dashboardversionId, Long userId)
//	{
//		return _dashboardversionRepository.findByDashboardversionIdAndUserId(dashboardversionId, userId);
//	}

	public List<DashboardversionEntity> findByUserId(Long userId)
	{
		return _dashboardversionRepository.findByUserId(userId);
	}
	
	public Page<DashboardversionEntity> findAll(Predicate predicate, Pageable pageable) {

		return _dashboardversionRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(DashboardversionId dashboardversionId) {
		
		Optional<DashboardversionEntity> dbDashboardversion= _dashboardversionRepository.findById(dashboardversionId);
		if(dbDashboardversion.isPresent()) {
			DashboardversionEntity existingDashboardversion = dbDashboardversion.get();
		    return existingDashboardversion.getUser();
		} else {
		    return null;
		}
	}
}

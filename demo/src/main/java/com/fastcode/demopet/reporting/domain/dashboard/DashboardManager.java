package com.fastcode.demopet.reporting.domain.dashboard;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.irepository.IDashboardRepository;
import com.fastcode.demopet.domain.irepository.IDashboardversionreportRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboard.dto.DashboardDetailsOutput;
import com.querydsl.core.types.Predicate;

@Repository
public class DashboardManager implements IDashboardManager {

    @Autowired
    IDashboardRepository  _dashboardRepository;
    
    @Autowired
	IDashboardversionreportRepository  _reportdashboardRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
	public DashboardEntity create(DashboardEntity dashboard) {

		return _dashboardRepository.save(dashboard);
	}

	public void delete(DashboardEntity dashboard) {

		_dashboardRepository.delete(dashboard);	
	}

	public DashboardEntity update(DashboardEntity dashboard) {

		return _dashboardRepository.save(dashboard);
	}

	public DashboardEntity findById(Long dashboardId) {
    	Optional<DashboardEntity> dbDashboard= _dashboardRepository.findById(dashboardId);
		if(dbDashboard.isPresent()) {
			DashboardEntity existingDashboard = dbDashboard.get();
		    return existingDashboard;
		} else {
		    return null;
		}

	}
	
	public DashboardEntity findByDashboardIdAndUserId(Long dashboardId, Long userId)
	{
		return _dashboardRepository.findByDashboardIdAndUserId(dashboardId, userId);
	}

	public Page<DashboardEntity> findAll(Predicate predicate, Pageable pageable) {

		return _dashboardRepository.findAll(predicate,pageable);
	}
	
	public List<DashboardEntity> findByUserId(Long userId)
	{
		return _dashboardRepository.findByUserId(userId);
	}

	public Page<DashboardDetailsOutput> getDashboards(Long userId, String search, Pageable pageable) throws Exception
	{
		Page<DashboardDetailsOutput> list = _dashboardRepository.getAllDashboardsByUserId(userId, search, pageable);
		return list;
	}
	
	public Page<DashboardDetailsOutput> getAvailableDashboards(Long userId, Long reportId, String search, Pageable pageable) throws Exception
	{
		Page<DashboardDetailsOutput> list = _dashboardRepository.getAvailableDashboardsByUserId(userId,reportId, search, pageable);
		return list;
	}
	
	public Page<DashboardDetailsOutput> getSharedDashboards(Long userId, String search, Pageable pageable) throws Exception
	{
		Page<DashboardDetailsOutput> list = _dashboardRepository.getSharedDashboardsByUserId(userId, search, pageable);
		return list;
	}
	
   //User
	public UserEntity getUser(Long dashboardId) {
		
		Optional<DashboardEntity> dbDashboard= _dashboardRepository.findById(dashboardId);
		if(dbDashboard.isPresent()) {
			DashboardEntity existingDashboard = dbDashboard.get();
		    return existingDashboard.getUser();
		} else {
		    return null;
		}
	}
}

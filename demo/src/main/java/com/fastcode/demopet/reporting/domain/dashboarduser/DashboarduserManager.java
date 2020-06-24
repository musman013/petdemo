package com.fastcode.demopet.reporting.domain.dashboarduser;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.irepository.IDashboarduserRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;

@Component
public class DashboarduserManager implements IDashboarduserManager {

    @Autowired
    IDashboarduserRepository  _dashboarduserRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    
	public DashboarduserEntity create(DashboarduserEntity dashboarduser) {

		return _dashboarduserRepository.save(dashboarduser);
	}

	public void delete(DashboarduserEntity dashboarduser) {

		_dashboarduserRepository.delete(dashboarduser);	
	}

	public DashboarduserEntity update(DashboarduserEntity dashboarduser) {

		return _dashboarduserRepository.save(dashboarduser);
	}

	public DashboarduserEntity findById(DashboarduserId dashboarduserId) {
    	Optional<DashboarduserEntity> dbDashboarduser= _dashboarduserRepository.findById(dashboarduserId);
		if(dbDashboarduser.isPresent()) {
			DashboarduserEntity existingDashboarduser = dbDashboarduser.get();
		    return existingDashboarduser;
		} else {
		    return null;
		}

	}
	
	public Page<UserEntity> getAvailableUsersList(Long dashboardId, String search, Pageable pageable) {
		return _dashboarduserRepository.getAvailableDashboardusersList(dashboardId, search, pageable);
	}
	
	public Page<UserEntity> getDashboardUsersList(Long dashboardId, String search, Pageable pageable) {
		return _dashboarduserRepository.getDashboardusersList(dashboardId, search, pageable);
	}
	
	public List<DashboarduserEntity> findByUserId(Long userId)
	{
		return _dashboarduserRepository.findByUserId(userId);
	}
	
	public List<DashboarduserEntity> findByDashboardId(Long dashboardId)
	{
		return _dashboarduserRepository.findByDashboardId(dashboardId);
	}
	
	public List<DashboarduserEntity> updateRefreshFlag(Long dashboardId, Boolean refresh)
	{
		return _dashboarduserRepository.updateRefreshFlag(refresh, dashboardId);
	}
	
	public List<DashboarduserEntity> updateOwnerSharingStatus(Long dashboardId, Boolean status)
	{
		return _dashboarduserRepository.updateOwnerSharingStatus(status, dashboardId);
	}


	public Page<DashboarduserEntity> findAll(Predicate predicate, Pageable pageable) {

		return _dashboarduserRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(DashboarduserId dashboarduserId) {
		
		Optional<DashboarduserEntity> dbDashboarduser= _dashboarduserRepository.findById(dashboarduserId);
		if(dbDashboarduser.isPresent()) {
			DashboarduserEntity existingDashboarduser = dbDashboarduser.get();
		    return existingDashboarduser.getUser();
		} else {
		    return null;
		}
	}
  
   //Dashboard
	public DashboardEntity getDashboard(DashboarduserId dashboarduserId) {
		
		Optional<DashboarduserEntity> dbDashboarduser= _dashboarduserRepository.findById(dashboarduserId);
		if(dbDashboarduser.isPresent()) {
			DashboarduserEntity existingDashboarduser = dbDashboarduser.get();
		    return existingDashboarduser.getDashboard();
		} else {
		    return null;
		}
	}
}

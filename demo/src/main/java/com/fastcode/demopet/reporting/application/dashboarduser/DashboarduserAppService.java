package com.fastcode.demopet.reporting.application.dashboarduser;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.domain.model.QDashboarduserEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboarduser.dto.*;
import com.fastcode.demopet.reporting.domain.dashboard.DashboardManager;
import com.fastcode.demopet.reporting.domain.dashboard.IDashboardManager;
import com.fastcode.demopet.reporting.domain.dashboarduser.IDashboarduserManager;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class DashboarduserAppService implements IDashboarduserAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IDashboarduserManager _dashboarduserManager;

    @Autowired
	private UserManager _userManager;
    
    @Autowired
	private IDashboardManager _dashboardManager;
    
	@Autowired
	private IDashboarduserMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateDashboarduserOutput create(CreateDashboarduserInput input) {

		DashboarduserEntity dashboarduser = mapper.createDashboarduserInputToDashboarduserEntity(input);
	  	if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
				dashboarduser.setUser(foundUser);
			}
		}
	  	if(input.getDashboardId()!=null) {
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			if(foundDashboard!=null) {
				dashboarduser.setDashboard(foundDashboard);
			}
		}
		DashboarduserEntity createdDashboarduser = _dashboarduserManager.create(dashboarduser);
		
		return mapper.dashboarduserEntityToCreateDashboarduserOutput(createdDashboarduser);
	}
    
    public Boolean addDashboardsToUser(UserEntity user, List<DashboardEntity> dashboardsList)
    {
    	Boolean status = true;
    	DashboarduserEntity dashboarduser = new DashboarduserEntity();
    	dashboarduser.setUserId(user.getId());
    	dashboarduser.setUser(user);
    
    	for(DashboardEntity dashboard : dashboardsList)
    	{
    		dashboarduser.setDashboard(dashboard);
    		dashboarduser.setDashboardId(dashboard.getId());
    		
    		DashboarduserEntity createdDashboarduser = _dashboarduserManager.create(dashboarduser);
    		
    		if(createdDashboarduser == null)
    		{
    		status = false;	
    		}
    	}
    	
    	return status;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Dashboarduser", key = "#p0")
	public UpdateDashboarduserOutput update(DashboarduserId dashboarduserId , UpdateDashboarduserInput input) {

		DashboarduserEntity dashboarduser = mapper.updateDashboarduserInputToDashboarduserEntity(input);
	  	if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
				dashboarduser.setUser(foundUser);
			}
		}
	  	if(input.getDashboardId()!=null) {
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			if(foundDashboard!=null) {
				dashboarduser.setDashboard(foundDashboard);
			}
		}
		
		DashboarduserEntity updatedDashboarduser = _dashboarduserManager.update(dashboarduser);
		
		return mapper.dashboarduserEntityToUpdateDashboarduserOutput(updatedDashboarduser);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Dashboarduser", key = "#p0")
	public void delete(DashboarduserId dashboarduserId) {

		DashboarduserEntity existing = _dashboarduserManager.findById(dashboarduserId) ; 
		_dashboarduserManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable(value = "Dashboarduser", key = "#p0")
	public FindDashboarduserByIdOutput findById(DashboarduserId dashboarduserId) {

		DashboarduserEntity foundDashboarduser = _dashboarduserManager.findById(dashboarduserId);
		if (foundDashboarduser == null)  
			return null ; 
 	   
 	    FindDashboarduserByIdOutput output=mapper.dashboarduserEntityToFindDashboarduserByIdOutput(foundDashboarduser); 
		return output;
	}
	
    //Dashboard
	// ReST API Call - GET /dashboarduser/1/dashboard
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @Cacheable (value = "Dashboarduser", key="#p0")
	public GetDashboardOutput getDashboard(DashboarduserId dashboarduserId) {

		DashboarduserEntity foundDashboarduser = _dashboarduserManager.findById(dashboarduserId);
		if (foundDashboarduser == null) {
			logHelper.getLogger().error("There does not exist a dashboarduser wth a id=%s", dashboarduserId);
			return null;
		}
		DashboardEntity re = _dashboarduserManager.getDashboard(dashboarduserId);
		return mapper.dashboardEntityToGetDashboardOutput(re, foundDashboarduser);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Dashboarduser")
	public List<FindDashboarduserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<DashboarduserEntity> foundDashboarduser = _dashboarduserManager.findAll(search(search), pageable);
		List<DashboarduserEntity> dashboarduserList = foundDashboarduser.getContent();
		Iterator<DashboarduserEntity> dashboarduserIterator = dashboarduserList.iterator(); 
		List<FindDashboarduserByIdOutput> output = new ArrayList<>();

		while (dashboarduserIterator.hasNext()) {
			output.add(mapper.dashboarduserEntityToFindDashboarduserByIdOutput(dashboarduserIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QDashboarduserEntity dashboarduser= QDashboarduserEntity.dashboarduserEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(dashboarduser, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("user") ||
				list.get(i).replace("%20","").trim().equals("userId") ||
				list.get(i).replace("%20","").trim().equals("dashboard") ||
				list.get(i).replace("%20","").trim().equals("dashboardId")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QDashboarduserEntity dashboarduser, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(dashboarduser.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("dashboardId")) {
		    builder.and(dashboarduser.dashboard.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public DashboarduserId parseDashboarduserKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		DashboarduserId dashboarduserId = new DashboarduserId();
		
		Map<String,String> keyMap = new HashMap<String,String>();
		if(keyEntries.length > 1) {
			for(String keyEntry: keyEntries)
			{
				String[] keyEntryArr = keyEntry.split(":");
				if(keyEntryArr.length > 1) {
					keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
				}
				else {
					return null;
				}
			}
		}
		else {
			return null;
		}
		
		dashboarduserId.setUserId(Long.valueOf(keyMap.get("userId")));
		dashboarduserId.setDashboardId(Long.valueOf(keyMap.get("dashboardId")));
		return dashboarduserId;
		
	}	
	
    
	
}



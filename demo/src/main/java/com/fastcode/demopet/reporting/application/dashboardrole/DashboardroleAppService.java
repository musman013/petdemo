package com.fastcode.demopet.reporting.application.dashboardrole;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.role.RoleManager;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboardroleEntity;
import com.fastcode.demopet.domain.model.DashboardroleId;
import com.fastcode.demopet.domain.model.QDashboardroleEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.reporting.application.dashboardrole.dto.*;
import com.fastcode.demopet.reporting.domain.dashboard.DashboardManager;
import com.fastcode.demopet.reporting.domain.dashboardrole.IDashboardroleManager;
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
public class DashboardroleAppService implements IDashboardroleAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IDashboardroleManager _dashboardroleManager;

    @Autowired
	private RoleManager _roleManager;
    
    @Autowired
	private DashboardManager _dashboardManager;
	@Autowired
	private IDashboardroleMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateDashboardroleOutput create(CreateDashboardroleInput input) {

		DashboardroleEntity dashboardrole = mapper.createDashboardroleInputToDashboardroleEntity(input);
	  	if(input.getRoleId()!=null) {
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());
			if(foundRole!=null) {
				dashboardrole.setRole(foundRole);
			}
		}
	  	if(input.getDashboardId()!=null) {
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			if(foundDashboard!=null) {
				dashboardrole.setDashboard(foundDashboard);
			}
		}
		DashboardroleEntity createdDashboardrole = _dashboardroleManager.create(dashboardrole);
		
		return mapper.dashboardroleEntityToCreateDashboardroleOutput(createdDashboardrole);
	}
    
    public Boolean addDashboardsToRole(RoleEntity role, List<DashboardEntity> dashboardsList)
    {
    	Boolean status = true;
    	DashboardroleEntity dashboardrole = new DashboardroleEntity();
    	dashboardrole.setRoleId(role.getId());
    	dashboardrole.setRole(role);
    
    	for(DashboardEntity dashboard : dashboardsList)
    	{
    		dashboardrole.setDashboard(dashboard);
    		dashboardrole.setDashboardId(dashboard.getId());
    		
    		DashboardroleEntity createdDashboardrole = _dashboardroleManager.create(dashboardrole);
    		
    		if(createdDashboardrole == null)
    		{
    		status = false;	
    		}
    	}
    	
    	return status;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Dashboardrole", key = "#p0")
	public UpdateDashboardroleOutput update(DashboardroleId dashboardroleId , UpdateDashboardroleInput input) {

		DashboardroleEntity dashboardrole = mapper.updateDashboardroleInputToDashboardroleEntity(input);
	  	if(input.getRoleId()!=null) {
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());
			if(foundRole!=null) {
				dashboardrole.setRole(foundRole);
			}
		}
	  	if(input.getDashboardId()!=null) {
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			if(foundDashboard!=null) {
				dashboardrole.setDashboard(foundDashboard);
			}
		}
		
		DashboardroleEntity updatedDashboardrole = _dashboardroleManager.update(dashboardrole);
		
		return mapper.dashboardroleEntityToUpdateDashboardroleOutput(updatedDashboardrole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Dashboardrole", key = "#p0")
	public void delete(DashboardroleId dashboardroleId) {

		DashboardroleEntity existing = _dashboardroleManager.findById(dashboardroleId) ; 
		_dashboardroleManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable(value = "Dashboardrole", key = "#p0")
	public FindDashboardroleByIdOutput findById(DashboardroleId dashboardroleId) {

		DashboardroleEntity foundDashboardrole = _dashboardroleManager.findById(dashboardroleId);
		if (foundDashboardrole == null)  
			return null ; 
 	   
 	    FindDashboardroleByIdOutput output=mapper.dashboardroleEntityToFindDashboardroleByIdOutput(foundDashboardrole); 
		return output;
	}
	
    //Dashboard
	// ReST API Call - GET /dashboardrole/1/dashboard
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @Cacheable (value = "Dashboardrole", key="#p0")
	public GetDashboardOutput getDashboard(DashboardroleId dashboardroleId) {

		DashboardroleEntity foundDashboardrole = _dashboardroleManager.findById(dashboardroleId);
		if (foundDashboardrole == null) {
			logHelper.getLogger().error("There does not exist a dashboardrole wth a id=%s", dashboardroleId);
			return null;
		}
		DashboardEntity re = _dashboardroleManager.getDashboard(dashboardroleId);
		return mapper.dashboardEntityToGetDashboardOutput(re, foundDashboardrole);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Dashboardrole")
	public List<FindDashboardroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<DashboardroleEntity> foundDashboardrole = _dashboardroleManager.findAll(search(search), pageable);
		List<DashboardroleEntity> dashboardroleList = foundDashboardrole.getContent();
		Iterator<DashboardroleEntity> dashboardroleIterator = dashboardroleList.iterator(); 
		List<FindDashboardroleByIdOutput> output = new ArrayList<>();

		while (dashboardroleIterator.hasNext()) {
			output.add(mapper.dashboardroleEntityToFindDashboardroleByIdOutput(dashboardroleIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QDashboardroleEntity dashboardrole= QDashboardroleEntity.dashboardroleEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(dashboardrole, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("role") ||
				list.get(i).replace("%20","").trim().equals("roleId") ||
				list.get(i).replace("%20","").trim().equals("dashboard") ||
				list.get(i).replace("%20","").trim().equals("dashboardId")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QDashboardroleEntity dashboardrole, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(dashboardrole.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("dashboardId")) {
		    builder.and(dashboardrole.dashboard.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public DashboardroleId parseDashboardroleKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		DashboardroleId dashboardroleId = new DashboardroleId();
		
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
		
		dashboardroleId.setRoleId(Long.valueOf(keyMap.get("roleId")));
		dashboardroleId.setDashboardId(Long.valueOf(keyMap.get("dashboardId")));
		return dashboardroleId;
		
	}	
	
    
	
}



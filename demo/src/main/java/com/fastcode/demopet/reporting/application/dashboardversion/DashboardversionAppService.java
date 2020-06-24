package com.fastcode.demopet.reporting.application.dashboardversion;


import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.QDashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.*;
import com.fastcode.demopet.reporting.domain.dashboard.DashboardManager;
import com.fastcode.demopet.reporting.domain.dashboardversion.IDashboardversionManager;
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
public class DashboardversionAppService implements IDashboardversionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IDashboardversionManager _dashboardversionManager;

    @Autowired
	private UserManager _userManager;
    
    @Autowired
   	private DashboardManager _dashboardManager;
    
	@Autowired
	private IDashboardversionMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateDashboardversionOutput create(CreateDashboardversionInput input) {

		DashboardversionEntity dashboardversion = mapper.createDashboardversionInputToDashboardversionEntity(input);
	  	if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
			//	dashboardversion.setUser(foundUser);
				foundUser.addDashboardversion(dashboardversion);
			}
		}
	  	if(input.getDashboardId()!=null) { 
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			if(foundDashboard!=null) {
			//	dashboardversion.setDashboard(foundDashboard);
				foundDashboard.addDashboardversion(dashboardversion);
			}
		}
	  	dashboardversion.setDashboardVersion("running");
		DashboardversionEntity createdRunningDashboardversion = _dashboardversionManager.create(dashboardversion);
		dashboardversion = mapper.createDashboardversionInputToDashboardversionEntity(input);
		dashboardversion.setDashboardVersion("published");
		DashboardversionEntity createdPublishedDashboardversion = _dashboardversionManager.create(dashboardversion);
		
		return mapper.dashboardversionEntityToCreateDashboardversionOutput(createdRunningDashboardversion);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Dashboardversion", key = "#p0")
	public UpdateDashboardversionOutput update(DashboardversionId dashboardversionId, UpdateDashboardversionInput input) {

		DashboardversionEntity dashboardversion = mapper.updateDashboardversionInputToDashboardversionEntity(input);
	  	
		if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
				dashboardversion.setUser(foundUser);
			}
		}
	  	
	  	if(input.getDashboardId()!=null) {
			DashboardEntity foundDashboard = _dashboardManager.findById(input.getDashboardId());
			dashboardversion.setDashboard(foundDashboard);
		}
		
	  	dashboardversion.setDashboardVersion(dashboardversionId.getDashboardVersion());
	  	DashboardversionEntity updatedDashboardversion = _dashboardversionManager.update(dashboardversion);

		return mapper.dashboardversionEntityToUpdateDashboardversionOutput(updatedDashboardversion);
		


	}
	
	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Dashboardversion", key = "#p0")
	public void delete(DashboardversionId dashboardversionId) {
		DashboardversionEntity existing = _dashboardversionManager.findById(dashboardversionId) ; 
		_dashboardversionManager.delete(existing);
		
	}
	
//	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Dashboardversion", key = "#p0")
//	public void delete(Long dashboardversionId, Long userId) {
//
//		DashboardversionEntity existing = _dashboardversionManager.findByDashboardversionIdAndUserId(dashboardversionId, userId);
//		_dashboardversionManager.delete(existing);
//		
//	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Dashboardversion", key = "#p0")
	public FindDashboardversionByIdOutput findById(DashboardversionId dashboardversionId) {

		DashboardversionEntity foundDashboardversion = _dashboardversionManager.findById(dashboardversionId);
		if (foundDashboardversion == null)  
			return null ; 
 	   
 	    FindDashboardversionByIdOutput output=mapper.dashboardversionEntityToFindDashboardversionByIdOutput(foundDashboardversion); 
		return output;
	}

    //User
	// ReST API Call - GET /dashboard/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @Cacheable (value = "Dashboardversion", key="#p0")
	public GetUserOutput getUser(DashboardversionId dashboardversionId) {

		DashboardversionEntity foundDashboardversion = _dashboardversionManager.findById(dashboardversionId);
		if (foundDashboardversion == null) {
			logHelper.getLogger().error("There does not exist a dashboardversion wth a id=%s", dashboardversionId);
			return null;
		}
		UserEntity re = _dashboardversionManager.getUser(dashboardversionId);
		return mapper.userEntityToGetUserOutput(re, foundDashboardversion);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Dashboardversion")
	public List<FindDashboardversionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<DashboardversionEntity> foundDashboardversion = _dashboardversionManager.findAll(search(search), pageable);
		List<DashboardversionEntity> dashboardversionList = foundDashboardversion.getContent();
		Iterator<DashboardversionEntity> dashboardversionIterator = dashboardversionList.iterator(); 
		List<FindDashboardversionByIdOutput> output = new ArrayList<>();

		while (dashboardversionIterator.hasNext()) {
			output.add(mapper.dashboardversionEntityToFindDashboardversionByIdOutput(dashboardversionIterator.next()));
		}
		return output;
	}
    
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QDashboardversionEntity dashboardversion= QDashboardversionEntity.dashboardversionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(dashboardversion, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("userId") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("reportdashboard") ||
				list.get(i).replace("%20","").trim().equals("title") ||
				list.get(i).replace("%20","").trim().equals("user")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QDashboardversionEntity dashboardversion, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(dashboardversion.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(dashboardversion.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(dashboardversion.description.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("title")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(dashboardversion.title.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(dashboardversion.title.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(dashboardversion.title.ne(details.getValue().getSearchValue()));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(dashboardversion.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	
	public Map<String,String> parseReportdashboardJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("dashboardversionId", keysString);
		return joinColumnMap;
	}
    
	
}



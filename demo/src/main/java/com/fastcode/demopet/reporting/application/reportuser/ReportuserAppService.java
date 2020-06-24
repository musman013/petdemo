package com.fastcode.demopet.reporting.application.reportuser;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.model.QReportuserEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.reportuser.dto.*;
import com.fastcode.demopet.reporting.domain.report.ReportManager;
import com.fastcode.demopet.reporting.domain.reportuser.IReportuserManager;
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
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class ReportuserAppService implements IReportuserAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IReportuserManager _reportuserManager;

    @Autowired
	private UserManager _userManager;
    
    @Autowired
	private ReportManager _reportManager;
    
	@Autowired
	private IReportuserMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateReportuserOutput create(CreateReportuserInput input) {

		ReportuserEntity reportuser = mapper.createReportuserInputToReportuserEntity(input);
	  	if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
				reportuser.setUser(foundUser);
			}
		}
	  	if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			if(foundReport!=null) {
				reportuser.setReport(foundReport);
			}
		}
		ReportuserEntity createdReportuser = _reportuserManager.create(reportuser);
		
		return mapper.reportuserEntityToCreateReportuserOutput(createdReportuser);
	}
    
    public Boolean addReportsToUser(UserEntity user, List<ReportEntity> reportsList)
    {
    	Boolean status = true;
    	ReportuserEntity reportuser = new ReportuserEntity();
    	reportuser.setUserId(user.getId());
    	reportuser.setUser(user);
    
    	for(ReportEntity report : reportsList)
    	{
    		reportuser.setReport(report);
    		reportuser.setReportId(report.getId());
    		
    		ReportuserEntity createdReportuser = _reportuserManager.create(reportuser);
    		
    		if(createdReportuser == null)
    		{
    		status = false;	
    		}
    	}
    	
    	return status;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Reportuser", key = "#p0")
	public UpdateReportuserOutput update(ReportuserId reportuserId , UpdateReportuserInput input) {

		ReportuserEntity reportuser = mapper.updateReportuserInputToReportuserEntity(input);
	  	if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			if(foundUser!=null) {
				reportuser.setUser(foundUser);
			}
		}
	  	if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			if(foundReport!=null) {
				reportuser.setReport(foundReport);
			}
		}
		
		ReportuserEntity updatedReportuser = _reportuserManager.update(reportuser);
		
		return mapper.reportuserEntityToUpdateReportuserOutput(updatedReportuser);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Reportuser", key = "#p0")
	public void delete(ReportuserId reportuserId) {

		ReportuserEntity existing = _reportuserManager.findById(reportuserId) ; 
		_reportuserManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable(value = "Reportuser", key = "#p0")
	public FindReportuserByIdOutput findById(ReportuserId reportuserId) {

		ReportuserEntity foundReportuser = _reportuserManager.findById(reportuserId);
		if (foundReportuser == null)  
			return null ; 
 	   
 	    FindReportuserByIdOutput output=mapper.reportuserEntityToFindReportuserByIdOutput(foundReportuser); 
		return output;
	}
	
    //Report
	// ReST API Call - GET /reportuser/1/report
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
//    @Cacheable (value = "Reportuser", key="#p0")
	public GetReportOutput getReport(ReportuserId reportuserId) {

		ReportuserEntity foundReportuser = _reportuserManager.findById(reportuserId);
		if (foundReportuser == null) {
			logHelper.getLogger().error("There does not exist a reportuser wth a id=%s", reportuserId);
			return null;
		}
		ReportEntity re = _reportuserManager.getReport(reportuserId);
		return mapper.reportEntityToGetReportOutput(re, foundReportuser);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Reportuser")
	public List<FindReportuserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<ReportuserEntity> foundReportuser = _reportuserManager.findAll(search(search), pageable);
		List<ReportuserEntity> reportuserList = foundReportuser.getContent();
		Iterator<ReportuserEntity> reportuserIterator = reportuserList.iterator(); 
		List<FindReportuserByIdOutput> output = new ArrayList<>();

		while (reportuserIterator.hasNext()) {
			output.add(mapper.reportuserEntityToFindReportuserByIdOutput(reportuserIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QReportuserEntity reportuser= QReportuserEntity.reportuserEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(reportuser, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("user") ||
				list.get(i).replace("%20","").trim().equals("userId") ||
				list.get(i).replace("%20","").trim().equals("report") ||
				list.get(i).replace("%20","").trim().equals("reportId")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QReportuserEntity reportuser, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(reportuser.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("reportId")) {
		    builder.and(reportuser.report.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public ReportuserId parseReportuserKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		ReportuserId reportuserId = new ReportuserId();
		
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
		
		reportuserId.setUserId(Long.valueOf(keyMap.get("userId")));
		reportuserId.setReportId(Long.valueOf(keyMap.get("reportId")));
		return reportuserId;
		
	}	
	
    
	
}



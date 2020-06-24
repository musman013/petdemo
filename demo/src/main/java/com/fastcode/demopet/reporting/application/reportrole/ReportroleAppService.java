package com.fastcode.demopet.reporting.application.reportrole;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.role.RoleManager;
import com.fastcode.demopet.domain.model.QReportroleEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportroleEntity;
import com.fastcode.demopet.domain.model.ReportroleId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.reporting.application.reportrole.dto.*;
import com.fastcode.demopet.reporting.domain.report.ReportManager;
import com.fastcode.demopet.reporting.domain.reportrole.IReportroleManager;
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
public class ReportroleAppService implements IReportroleAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IReportroleManager _reportroleManager;

    @Autowired
	private RoleManager _roleManager;
    
    @Autowired
	private ReportManager _reportManager;
    
	@Autowired
	private IReportroleMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateReportroleOutput create(CreateReportroleInput input) {

		ReportroleEntity reportrole = mapper.createReportroleInputToReportroleEntity(input);
	  	if(input.getRoleId()!=null) {
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());
			if(foundRole!=null) {
				reportrole.setRole(foundRole);
			}
		}
	  	if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			if(foundReport!=null) {
				reportrole.setReport(foundReport);
			}
		}
		ReportroleEntity createdReportrole = _reportroleManager.create(reportrole);
		
		return mapper.reportroleEntityToCreateReportroleOutput(createdReportrole);
	}
    
    public Boolean addReportsToRole(RoleEntity role, List<ReportEntity> reportsList)
    {
    	Boolean status = true;
    	ReportroleEntity reportrole = new ReportroleEntity();
    	reportrole.setRoleId(role.getId());
    	reportrole.setRole(role);
    
    	for(ReportEntity report : reportsList)
    	{
    		reportrole.setReport(report);
    		reportrole.setReportId(report.getId());
    		
    		ReportroleEntity createdReportrole = _reportroleManager.create(reportrole);
    		
    		if(createdReportrole == null)
    		{
    		status = false;	
    		}
    	}
    	
    	return status;
    }
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Reportrole", key = "#p0")
	public UpdateReportroleOutput update(ReportroleId reportroleId , UpdateReportroleInput input) {

		ReportroleEntity reportrole = mapper.updateReportroleInputToReportroleEntity(input);
	  	if(input.getRoleId()!=null) {
			RoleEntity foundRole = _roleManager.findById(input.getRoleId());
			if(foundRole!=null) {
				reportrole.setRole(foundRole);
			}
		}
	  	if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			if(foundReport!=null) {
				reportrole.setReport(foundReport);
			}
		}
		
		ReportroleEntity updatedReportrole = _reportroleManager.update(reportrole);
		
		return mapper.reportroleEntityToUpdateReportroleOutput(updatedReportrole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Reportrole", key = "#p0")
	public void delete(ReportroleId reportroleId) {

		ReportroleEntity existing = _reportroleManager.findById(reportroleId) ; 
		_reportroleManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Reportrole", key = "#p0")
	public FindReportroleByIdOutput findById(ReportroleId reportroleId) {

		ReportroleEntity foundReportrole = _reportroleManager.findById(reportroleId);
		if (foundReportrole == null)  
			return null ; 
 	   
 	    FindReportroleByIdOutput output=mapper.reportroleEntityToFindReportroleByIdOutput(foundReportrole); 
		return output;
	}

	
    //Report
	// ReST API Call - GET /reportrole/1/report
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Reportrole", key="#p0")
	public GetReportOutput getReport(ReportroleId reportroleId) {

		ReportroleEntity foundReportrole = _reportroleManager.findById(reportroleId);
		if (foundReportrole == null) {
			logHelper.getLogger().error("There does not exist a reportrole wth a id=%s", reportroleId);
			return null;
		}
		ReportEntity re = _reportroleManager.getReport(reportroleId);
		return mapper.reportEntityToGetReportOutput(re, foundReportrole);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Reportrole")
	public List<FindReportroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<ReportroleEntity> foundReportrole = _reportroleManager.findAll(search(search), pageable);
		List<ReportroleEntity> reportroleList = foundReportrole.getContent();
		Iterator<ReportroleEntity> reportroleIterator = reportroleList.iterator(); 
		List<FindReportroleByIdOutput> output = new ArrayList<>();

		while (reportroleIterator.hasNext()) {
			output.add(mapper.reportroleEntityToFindReportroleByIdOutput(reportroleIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QReportroleEntity reportrole= QReportroleEntity.reportroleEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(reportrole, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("role") ||
				list.get(i).replace("%20","").trim().equals("roleId") ||
				list.get(i).replace("%20","").trim().equals("report") ||
				list.get(i).replace("%20","").trim().equals("reportId")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QReportroleEntity reportrole, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("editable")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(reportrole.editable.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(reportrole.editable.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(reportrole.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("reportId")) {
		    builder.and(reportrole.report.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public ReportroleId parseReportroleKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		ReportroleId reportroleId = new ReportroleId();
		
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
		
		reportroleId.setRoleId(Long.valueOf(keyMap.get("roleId")));
		reportroleId.setReportId(Long.valueOf(keyMap.get("reportId")));
		return reportroleId;
		
	}	
	
    
	
}



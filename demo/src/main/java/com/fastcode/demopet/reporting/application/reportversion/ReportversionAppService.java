package com.fastcode.demopet.reporting.application.reportversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.QReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.reportversion.IReportversionAppService;
import com.fastcode.demopet.reporting.application.reportversion.IReportversionMapper;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.FindReportversionByIdOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.GetUserOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionOutput;
import com.fastcode.demopet.reporting.domain.report.ReportManager;
import com.fastcode.demopet.reporting.domain.reportversion.IReportversionManager;
import com.querydsl.core.BooleanBuilder;

@Service
@Validated
public class ReportversionAppService implements IReportversionAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private IReportversionManager _reportversionManager;

	@Autowired
	private UserManager _userManager;

	@Autowired
	private ReportManager _reportManager;

	@Autowired
	private IReportversionMapper mapper;

	@Autowired
	private LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateReportversionOutput create(CreateReportversionInput input) {

		ReportversionEntity reportversion = mapper.createReportversionInputToReportversionEntity(input);
		if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			reportversion.setUser(foundUser);
		}

		if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			reportversion.setReport(foundReport);
		}

		reportversion.setReportVersion("running");
		ReportversionEntity createdRunningReportversion = _reportversionManager.create(reportversion);

		reportversion = mapper.createReportversionInputToReportversionEntity(input);
		reportversion.setReportVersion("published");
		ReportversionEntity createdPublishedReportversion = _reportversionManager.create(reportversion);

		return mapper.reportversionEntityToCreateReportversionOutput(createdRunningReportversion);
	}

	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Reportversion", key = "#p0")
	public UpdateReportversionOutput update(ReportversionId reportversionId, UpdateReportversionInput input) {

		ReportversionEntity reportversion = mapper.updateReportversionInputToReportversionEntity(input);

		if(input.getUserId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getUserId());
			reportversion.setUser(foundUser);
		}

		if(input.getReportId()!=null) {
			ReportEntity foundReport = _reportManager.findById(input.getReportId());
			reportversion.setReport(foundReport);
		}

		reportversion.setReportVersion(reportversionId.getReportVersion());
		ReportversionEntity updatedReportversion = _reportversionManager.update(reportversion);

		return mapper.reportversionEntityToUpdateReportversionOutput(updatedReportversion);
	}

	@Transactional(propagation = Propagation.REQUIRED)
//	@CacheEvict(value="Reportversion", key = "#p0")
	public void delete(ReportversionId reportversionId) {

		ReportversionEntity existing = _reportversionManager.findById(reportversionId) ; 
		_reportversionManager.delete(existing);

	}

	//	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="ReportVersion", key = "#p0")
	//	public void delete(Long reportId, Long userId) {
	//
	//		ReportEntity existing = _reportManager.findByReportIdAndUserId(reportId, userId);
	//		_reportManager.delete(existing);
	//		
	//	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable(value = "Reportversion", key = "#p0")
	public FindReportversionByIdOutput findById(ReportversionId reportversionId) {

		ReportversionEntity foundReportversion = _reportversionManager.findById(reportversionId);
		if (foundReportversion == null)  
			return null ; 

		FindReportversionByIdOutput output=mapper.reportversionEntityToFindReportversionByIdOutput(foundReportversion); 
		return output;
	}
	//User
	// ReST API Call - GET /reportversion/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable (value = "Reportversion", key="#p0")
	public GetUserOutput getUser(ReportversionId reportversionId) {

		ReportversionEntity foundReportversion = _reportversionManager.findById(reportversionId);
		if (foundReportversion == null) {
			logHelper.getLogger().error("There does not exist a reportversion wth a id=%s", reportversionId);
			return null;
		}
		UserEntity re = _reportversionManager.getUser(reportversionId);
		return mapper.userEntityToGetUserOutput(re, foundReportversion);
	}

//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
//	@Cacheable(value = "Reportversion", key = "#p0")
//	public FindReportversionByIdOutput findByReportIdAndVersionAndUserId(ReportversionId reportversionId) {
//
//		ReportversionEntity foundReportversion = _reportversionManager.findByReportIdAndVersionAndUserId(reportversionId);
//		if (foundReportversion == null)  
//			return null ; 
//
//		FindReportversionByIdOutput output = mapper.reportversionEntityToFindReportversionByIdOutput(foundReportversion); 
//		return output;
//	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Reportversion", key = "#p0")
	public List<FindReportversionByIdOutput> findByUserId(Long userId) {

		List<ReportversionEntity> reportList = _reportversionManager.findByUserId(userId);
		if (reportList == null)  
			return null ; 

		Iterator<ReportversionEntity> reportIterator = reportList.iterator(); 
		List<FindReportversionByIdOutput> output = new ArrayList<>();

		while (reportIterator.hasNext()) {
			output.add(mapper.reportversionEntityToFindReportversionByIdOutput(reportIterator.next()));
		}

		return output;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Reportversion")
	public List<FindReportversionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<ReportversionEntity> foundReportversion = _reportversionManager.findAll(search(search), pageable);
		List<ReportversionEntity> reportList = foundReportversion.getContent();
		Iterator<ReportversionEntity> reportIterator = reportList.iterator(); 
		List<FindReportversionByIdOutput> output = new ArrayList<>();

		while (reportIterator.hasNext()) {
			output.add(mapper.reportversionEntityToFindReportversionByIdOutput(reportIterator.next()));
		}
		return output;
	}

	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QReportversionEntity reportversion= QReportversionEntity.reportversionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(reportversion, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("userId") ||
					list.get(i).replace("%20","").trim().equals("ctype") ||
					list.get(i).replace("%20","").trim().equals("description") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("query") ||
					list.get(i).replace("%20","").trim().equals("reportType") ||
					list.get(i).replace("%20","").trim().equals("reportdashboard") ||
					list.get(i).replace("%20","").trim().equals("title") ||
					list.get(i).replace("%20","").trim().equals("user")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchKeyValuePair(QReportversionEntity reportversion, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("ctype")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(reportversion.ctype.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(reportversion.ctype.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(reportversion.ctype.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(reportversion.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(reportversion.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(reportversion.description.ne(details.getValue().getSearchValue()));
			}

			if(details.getKey().replace("%20","").trim().equals("reportType")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(reportversion.reportType.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(reportversion.reportType.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(reportversion.reportType.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("title")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(reportversion.title.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(reportversion.title.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(reportversion.title.ne(details.getValue().getSearchValue()));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("userId")) {
				builder.and(reportversion.user.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}


	public Map<String,String> parsedashboardversionreportJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("reportId", keysString);
		return joinColumnMap;
	}


}

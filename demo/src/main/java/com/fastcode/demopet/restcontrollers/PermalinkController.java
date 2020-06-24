package com.fastcode.demopet.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboard.IDashboardAppService;
import com.fastcode.demopet.reporting.application.dashboard.dto.FindDashboardByIdOutput;
import com.fastcode.demopet.reporting.application.dashboarduser.IDashboarduserAppService;
import com.fastcode.demopet.reporting.application.dashboarduser.dto.FindDashboarduserByIdOutput;
import com.fastcode.demopet.reporting.application.permalink.PermalinkAppService;
import com.fastcode.demopet.reporting.application.permalink.dto.*;
import com.fastcode.demopet.reporting.application.report.IReportAppService;
import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.demopet.reporting.application.reportuser.IReportuserAppService;
import com.fastcode.demopet.reporting.application.reportuser.dto.FindReportuserByIdOutput;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/permalink")
public class PermalinkController {

	@Autowired
	private PermalinkAppService _permalinkAppService;

	@Autowired
	private IReportuserAppService _reportuserAppService;

	@Autowired
	private IReportAppService _reportAppService;
	
	@Autowired
	private IDashboarduserAppService _dashboarduserAppService;
	
	@Autowired
	private IDashboardAppService _dashboardAppService;

	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;


	public PermalinkController(PermalinkAppService permalinkAppService,
			LoggingHelper helper) {
		super();
		this._permalinkAppService = permalinkAppService;
		this.logHelper = helper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreatePermalinkOutput> create(@RequestBody @Valid CreatePermalinkInput permalink) {

		UserEntity user = _userAppService.getUser();
		if(permalink.getResource() == "report") {
			FindReportByIdOutput report = _reportAppService.findById(Long.valueOf(permalink.getResourceId()));
			Optional.ofNullable(report).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a report with a id=%s", permalink.getResourceId())));
			
			FindReportuserByIdOutput reportuser = _reportuserAppService.findById(new ReportuserId(Long.valueOf(permalink.getResourceId()), user.getId()));

			if(report.getOwnerId() != user.getId() &&  reportuser == null) {
				logHelper.getLogger().error("You do not have access to the report with a id=%s", permalink.getResourceId());
				throw new EntityNotFoundException(
						String.format("You do not have access to the report with a id=%s", permalink.getResourceId()));
			}
		}
		if(permalink.getResource() == "dashboard") {
			FindDashboardByIdOutput dashboard = _dashboardAppService.findById(Long.valueOf(permalink.getResourceId()));
			Optional.ofNullable(dashboard).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a dashboard with a id=%s", permalink.getResourceId())));
			
			FindDashboarduserByIdOutput dashboarduser = _dashboarduserAppService.findById(new DashboarduserId(Long.valueOf(permalink.getResourceId()), user.getId()));
			
			if(dashboard.getOwnerId() != user.getId() &&  dashboarduser == null) {
				logHelper.getLogger().error("You do not have access to the dashboard with a id=%s", permalink.getResourceId());
				throw new EntityNotFoundException(
						String.format("You do not have access to the dashboard with a id=%s", permalink.getResourceId()));
			}
		}
		CreatePermalinkOutput output=_permalinkAppService.create(permalink);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete permalink ------------
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable UUID id) {
		
		FindPermalinkByIdOutput output = _permalinkAppService.findById(id);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a permalink with a id=%s", id)));
		
		_permalinkAppService.delete(id);
	}

	// ------------ Update permalink ------------
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdatePermalinkOutput> update(@PathVariable UUID id, @RequestBody @Valid UpdatePermalinkInput permalink) {
		
		FindPermalinkByIdOutput currentPermalink = _permalinkAppService.findById(id);
		Optional.ofNullable(currentPermalink).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Permalink with id=%snot found.", id)));
		
		return new ResponseEntity(_permalinkAppService.update(id,permalink), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindPermalinkByIdOutput> findById(@PathVariable UUID id) {
		
		FindPermalinkByIdOutput output = _permalinkAppService.findById(id);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a permalink with a id=%s", id)));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{resource}/{resourceId}", method = RequestMethod.GET)
	public ResponseEntity<FindPermalinkByIdOutput> getPermalinkbyResource(@PathVariable String resource, @PathVariable String resourceId) {
		
		FindPermalinkByIdOutput output = _permalinkAppService.findByResourceAndResourceId(Long.valueOf(resourceId), resource);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a resource with a id=%s", Long.valueOf(resourceId))));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(_permalinkAppService.find(searchCriteria,Pageable));
	}


}


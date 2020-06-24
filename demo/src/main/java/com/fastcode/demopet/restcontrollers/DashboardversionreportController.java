package com.fastcode.demopet.restcontrollers;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
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

import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.domain.model.DashboardversionreportId;
import com.fastcode.demopet.reporting.application.dashboard.DashboardAppService;
import com.fastcode.demopet.reporting.application.dashboardversionreport.DashboardversionreportAppService;
import com.fastcode.demopet.reporting.application.dashboardversionreport.dto.*;
import com.fastcode.demopet.reporting.application.report.ReportAppService;
@RestController
@RequestMapping("/reportdashboard")
public class DashboardversionreportController {

	@Autowired
	private DashboardversionreportAppService _reportdashboardAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	public DashboardversionreportController(DashboardversionreportAppService reportdashboardAppService, DashboardAppService dashboardAppService, ReportAppService reportAppService,
			LoggingHelper helper) {
		super();
		this._reportdashboardAppService = reportdashboardAppService;
		this.logHelper = helper;
	}

	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateDashboardversionreportOutput> create(@RequestBody @Valid CreateDashboardversionreportInput reportdashboard) {
		CreateDashboardversionreportOutput output=_reportdashboardAppService.create(reportdashboard);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete reportdashboard ------------
	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
		
		DashboardversionreportId reportdashboardid =_reportdashboardAppService.parseReportdashboardKey(id);
		Optional.ofNullable(reportdashboardid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindDashboardversionreportByIdOutput output = _reportdashboardAppService.findById(reportdashboardid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a reportdashboard with a id=%s", id)));

		_reportdashboardAppService.delete(reportdashboardid);
	}

	// ------------ Update reportdashboard ------------
	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateDashboardversionreportOutput> update(@PathVariable String id, @RequestBody @Valid UpdateDashboardversionreportInput reportdashboard) {
		
		DashboardversionreportId reportdashboardid =_reportdashboardAppService.parseReportdashboardKey(id);
		Optional.ofNullable(reportdashboardid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindDashboardversionreportByIdOutput currentReportdashboard = _reportdashboardAppService.findById(reportdashboardid);
		Optional.ofNullable(currentReportdashboard).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Reportdashboard with id=%s not found.", id)));

		reportdashboard.setVersion(currentReportdashboard.getVersion());
		return new ResponseEntity(_reportdashboardAppService.update(reportdashboardid,reportdashboard), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindDashboardversionreportByIdOutput> findById(@PathVariable String id) {
		
		DashboardversionreportId reportdashboardid =_reportdashboardAppService.parseReportdashboardKey(id);
		Optional.ofNullable(reportdashboardid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		FindDashboardversionreportByIdOutput output = _reportdashboardAppService.findById(reportdashboardid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		return ResponseEntity.ok(_reportdashboardAppService.find(searchCriteria,Pageable));
	}
	
	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}/dashboard", method = RequestMethod.GET)
	public ResponseEntity<GetDashboardversionOutput> getDashboard(@PathVariable String id) {
		
		DashboardversionreportId reportdashboardid =_reportdashboardAppService.parseReportdashboardKey(id);
		Optional.ofNullable(reportdashboardid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		GetDashboardversionOutput output= _reportdashboardAppService.getDashboard(reportdashboardid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('REPORTDASHBOARDENTITY_READ')")
	@RequestMapping(value = "/{id}/report", method = RequestMethod.GET)
	public ResponseEntity<GetReportOutput> getReport(@PathVariable String id) {
		
		DashboardversionreportId reportdashboardid =_reportdashboardAppService.parseReportdashboardKey(id);
		Optional.ofNullable(reportdashboardid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));

		GetReportOutput output= _reportdashboardAppService.getReport(reportdashboardid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}
	
}


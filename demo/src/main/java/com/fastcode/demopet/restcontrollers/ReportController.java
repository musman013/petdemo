package com.fastcode.demopet.restcontrollers;

import javax.persistence.EntityExistsException;
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

import com.fastcode.demopet.application.authorization.role.RoleAppService;
import com.fastcode.demopet.application.authorization.role.dto.FindRoleByIdOutput;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.reporting.application.dashboardversionreport.DashboardversionreportAppService;
import com.fastcode.demopet.reporting.application.dashboardversionreport.dto.FindDashboardversionreportByIdOutput;
import com.fastcode.demopet.reporting.application.report.ReportAppService;
import com.fastcode.demopet.reporting.application.report.dto.*;
import com.fastcode.demopet.reporting.application.reportuser.ReportuserAppService;
import com.fastcode.demopet.reporting.application.reportuser.dto.FindReportuserByIdOutput;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/report")
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReportController {

	@Autowired
	private ReportAppService _reportAppService;

	@Autowired
	private ReportuserAppService _reportuserAppService;

	@Autowired
	private RoleAppService _roleAppService;

	@Autowired
	private DashboardversionreportAppService  _reportdashboardAppService;

	@Autowired
	private UserAppService  _userAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	public ReportController(ReportAppService reportAppService, DashboardversionreportAppService reportdashboardAppService, UserAppService userAppService,
			LoggingHelper helper) {
		super();
		this._reportAppService = reportAppService;
		this._reportdashboardAppService = reportdashboardAppService;
		this._userAppService = userAppService;
		this.logHelper = helper;
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateReportOutput> create(@RequestBody @Valid CreateReportInput report) {
		UserEntity user = _userAppService.getUser();
		report.setOwnerId(user.getId());
		report.setIsPublished(true);
		report.setIsAssignedByDashboard(false);
		CreateReportOutput output=_reportAppService.create(report);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete report ------------  
	@PreAuthorize("hasAnyAuthority('REPORTENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput output = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a report with a id=%s", id)));
		
		FindReportuserByIdOutput reportuser = _reportuserAppService.findById(new ReportuserId(Long.valueOf(id), user.getId()));
		
		if(output.getOwnerId() != user.getId() &&  reportuser == null) {
			logHelper.getLogger().error("You do not have access to update a report with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update a report with a id=%s", id));
		}

		_reportAppService.delete(Long.valueOf(id), user.getId());
	}

	// ------------ Update report ------------
	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateReportOutput> update(@PathVariable String id, @RequestBody @Valid UpdateReportInput report) {
		
		UserEntity user = _userAppService.getUser();

		FindReportByIdOutput currentReport = _reportAppService.findById(report.getId());
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Report with id=%s not found.", id)));

		FindReportuserByIdOutput reportuser = _reportuserAppService.findById(new ReportuserId(Long.valueOf(id), user.getId()));

		if(currentReport.getOwnerId() != user.getId() &&  reportuser == null) {
			logHelper.getLogger().error("You do not have access to update a report with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update a report with a id=%s", id));
		}

        report.setUserId(user.getId());
        report.setVersion(currentReport.getVersion());
        
		return new ResponseEntity(_reportAppService.update(Long.valueOf(id),report), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindReportByIdOutput> findById(@PathVariable String id) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput output = _reportAppService.findByReportIdAndUserId(Long.valueOf(id), user.getId());
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}

	//	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	//	@RequestMapping(method = RequestMethod.GET)
	//	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
	//		UserEntity user = _userAppService.getUser();
	//
	//		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
	//		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
	//
	//		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
	//
	//		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
	//		Map<String,String> joinColDetails=_userAppService.parseReportJoinColumn(user.getId().toString());
	//		if(joinColDetails== null)
	//		{
	//			logHelper.getLogger().error("Invalid Join Column");
	//			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	//		}
	//		searchCriteria.setJoinColumns(joinColDetails);
	//
	//		List<FindReportByIdOutput> output = _reportAppService.find(searchCriteria,pageable);
	//		if (output == null) {
	//			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	//		}
	//
	//		return new ResponseEntity(output, HttpStatus.OK);
	//	}   

	//	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	//	@RequestMapping(method = RequestMethod.GET)
	//	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
	//		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
	//		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
	//
	//		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
	//		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
	//
	//		return ResponseEntity.ok(_reportAppService.find(searchCriteria,Pageable));
	//	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}/reportdashboard", method = RequestMethod.GET)
	public ResponseEntity getReportdashboard(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);

		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_reportAppService.parseReportdashboardJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid Join Column")));

		searchCriteria.setJoinColumns(joinColDetails);

		List<FindDashboardversionreportByIdOutput> output = _reportdashboardAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));

		return new ResponseEntity(output, HttpStatus.OK);
	}   

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<List<GetUserOutput>> getUser(@PathVariable String id,@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to get users for report with id '{}'.", id);
			throw new EntityNotFoundException(
					String.format("Unable to get users for report with id '{}'.", id));
		}

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset),Integer.parseInt(limit), sort);
		List<GetUserOutput> output = _reportAppService.getUsersAssociatedWithReport(Long.valueOf(id),search,pageable);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}/availableUser", method = RequestMethod.GET)
	public ResponseEntity<List<GetUserOutput>> getAvailableUser(@PathVariable String id,@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to get users for report with id {}.", id);
			throw new EntityNotFoundException(
					String.format("Unable to users for report with id {}.", id));
		}

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<GetUserOutput> output = _reportAppService.getAvailableUsers(Long.valueOf(id),search,pageable);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}/role", method = RequestMethod.GET)
	public ResponseEntity<List<GetRoleOutput>> getRole(@PathVariable String id,@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to get users for report with id {}.", id);
			throw new EntityNotFoundException(
					String.format("Unable to users for report with id {}.", id));
		}

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<GetRoleOutput> output = _reportAppService.getRolesAssociatedWithReport(Long.valueOf(id),search,pageable);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/{id}/availableRole", method = RequestMethod.GET)
	public ResponseEntity<List<GetRoleOutput>> getAvailableRole(@PathVariable String id,@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to get users for report with id {}.", id);
			throw new EntityNotFoundException(
					String.format("Unable to get users for report with id {}.", id));
		}

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<GetRoleOutput> output = _reportAppService.getAvailableRoles(Long.valueOf(id),search,pageable);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ReportDetailsOutput>> getReport(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		UserEntity user = _userAppService.getUser();

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<ReportDetailsOutput> output = _reportAppService.getReports(user.getId(), search,pageable);

		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_READ')")
	@RequestMapping(value = "/shared" ,method = RequestMethod.GET)
	public ResponseEntity<List<ReportDetailsOutput>> getSharedReport(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		UserEntity user = _userAppService.getUser();

		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		List<ReportDetailsOutput> output = _reportAppService.getSharedReports(user.getId(), search,pageable);

		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/editAccess", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> editReportAccess(@PathVariable String id, @RequestBody @Valid Map<String, List<ShareReportInput>> input) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to share report with id '{}'.", id);
			throw new EntityNotFoundException(
					String.format("Unable to share report with id {}.", id));
		}

		List<ShareReportInput> usersList = input.get("users");
		List<ShareReportInput> rolesList = input.get("roles");

		for(ShareReportInput roleInput : rolesList)
		{
			FindRoleByIdOutput foundRole = _roleAppService.findById(roleInput.getId());
			Optional.ofNullable(foundRole).orElseThrow(() -> new EntityNotFoundException(String.format("Role not found with id=%s", roleInput.getId())));
		}

		for(ShareReportInput userInput : usersList)
		{
			FindUserByIdOutput foundUser = _userAppService.findById(userInput.getId());
			Optional.ofNullable(foundUser).orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id=%s", userInput.getId())));
		}
		
		ReportDetailsOutput output = _reportAppService.editReportAccess(Long.valueOf(id),usersList, rolesList);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to found user published version with id=%s", id)));
			
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/share", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> shareReport(@PathVariable String id, @RequestBody @Valid Map<String, List<ShareReportInput>> input) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Report with id=%s not found.", id)));

		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
			logHelper.getLogger().error("Unable to share report with id '{}'.", id);
			throw new EntityNotFoundException(
					String.format("Unable to share report with id {}.", id));
		}

		List<ShareReportInput> usersList = input.get("users");
		List<ShareReportInput> rolesList = input.get("roles");

		for(ShareReportInput roleInput : rolesList)
		{
			FindRoleByIdOutput foundRole = _roleAppService.findById(roleInput.getId());
			Optional.ofNullable(foundRole).orElseThrow(() -> new EntityNotFoundException(String.format("Role not found with id=%s", roleInput.getId())));
		}

		for(ShareReportInput userInput : usersList)
		{
			FindUserByIdOutput foundUser = _userAppService.findById(userInput.getId());
			Optional.ofNullable(foundUser).orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id=%s", userInput.getId())));
		}

		ReportDetailsOutput output = _reportAppService.shareReport(Long.valueOf(id),false, usersList, rolesList);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to found user published version with id=%s", id)));
			
		return new ResponseEntity(output, HttpStatus.OK);
	}

//	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
//	@RequestMapping(value = "/{id}/unshare", method = RequestMethod.PUT)
//	public ResponseEntity<ReportDetailsOutput> unshareReport(@PathVariable String id, @RequestBody @Valid Map<String, List<ShareReportInput>> input) {
//		UserEntity user = _userAppService.getUser();
//		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
//
//		if (currentReport == null) {
//			logHelper.getLogger().error("Report with id {} not found.", id);
//			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
//		}
//
//		if (currentReport.getOwnerId() == null || user.getId() != currentReport.getOwnerId()) {
//			logHelper.getLogger().error("Unable to unshare report with id {}.", id);
//			throw new EntityNotFoundException(
//					String.format("Unable to unshare report with id {}.", id));
//		}
//
//		List<ShareReportInput> usersList = input.get("users");
//		List<ShareReportInput> rolesList = input.get("roles");
//
//		for(ShareReportInput roleInput : rolesList)
//		{
//			FindRoleByIdOutput foundRole = _roleAppService.findById(roleInput.getId());
//			if(foundRole == null)
//			{
//				logHelper.getLogger().error("Role not found with id=%s", roleInput.getId());
//				throw new EntityNotFoundException(
//						String.format("Role not found with id=%s", roleInput.getId()));
//			}
//		}
//
//		for(ShareReportInput userInput : usersList)
//		{
//			FindUserByIdOutput foundUser = _userAppService.findById(userInput.getId());
//			if(foundUser == null)
//			{
//				logHelper.getLogger().error("User not found with id=%s", userInput.getId());
//				throw new EntityNotFoundException(
//						String.format("User not found with id=%s", userInput.getId()));
//			}
//		}
//
//		ReportDetailsOutput output = _reportAppService.unshareReport(Long.valueOf(id), usersList, rolesList);
//        if(output == null)
//        {
//        	logHelper.getLogger().error("Unable to found user published version with id=%s", id);
//        	throw new EntityNotFoundException(
//					String.format("Unable to found user published version with id=%s", id));
//        }
//		return new ResponseEntity(output, HttpStatus.OK);
//	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/updateRecipientSharingStatus", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> updateRecipientSharingStatus(@PathVariable String id, @RequestBody Boolean status) {
		
		UserEntity user = _userAppService.getUser();

		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Report with id=%s not found.", id)));

		ReportDetailsOutput output = _reportAppService.updateRecipientSharingStatus(user.getId(), Long.valueOf(id), status);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Report user does't exists")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/publish", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> publishReport(@PathVariable String id) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Report with id=%s not found.", id)));
		
		if(!user.getId().equals(currentReport.getOwnerId())) {
			logHelper.getLogger().error("You do not have access to publish a report with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to publish a report with a id=%s", id));
		}

		if(currentReport.getIsPublished())
		{
			logHelper.getLogger().error("Report is already published with a id=%s", id);
			throw new EntityExistsException(
					String.format("Report is already published with a id=%s", id));

		}

		ReportDetailsOutput output = _reportAppService.publishReport(user.getId(), Long.valueOf(id));
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/changeOwner", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> changeOwner(@PathVariable String id,@RequestBody Long userId) {
		
		UserEntity user = _userAppService.getUser();
		FindReportByIdOutput currentReport = _reportAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentReport).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Report with id=%s not found.", id)));

		if(!user.getId().equals(currentReport.getOwnerId())) {
			logHelper.getLogger().error("You do not have access to update owner of a report with a id=%s", id);
			throw new EntityNotFoundException(
					String.format("You do not have access to update owner of a report with a id=%s", id));
		}

		if(currentReport.getOwnerId() == userId)
		{
			logHelper.getLogger().error("Report is already owned by user with id= %s", id);
			throw new EntityExistsException(
					String.format("Report is already owned by userwith a id=%s", id));
		}

		if(_userAppService.findById(userId) == null)
		{
			logHelper.getLogger().error("User does not exist with id=%s", id);
			throw new EntityNotFoundException(
					String.format("User does not exist with id=%s", id));
		}
		
		return new ResponseEntity(_reportAppService.changeOwner(currentReport.getOwnerId(), Long.valueOf(id), userId), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/refresh", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> refreshReport(@PathVariable String id) {
		
		UserEntity user = _userAppService.getUser();

		FindReportuserByIdOutput reportuser = _reportuserAppService.findById(new ReportuserId(Long.valueOf(id), user.getId()));
		Optional.ofNullable(reportuser).orElseThrow(() -> new EntityNotFoundException(String.format("No Report is shared with id=%s", id)));
		
		ReportDetailsOutput output = _reportAppService.refreshReport(user.getId(), Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No updates available. Report can not be refreshed")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('REPORTENTITY_UPDATE')")
	@RequestMapping(value = "/{id}/reset", method = RequestMethod.PUT)
	public ResponseEntity<ReportDetailsOutput> resetReport(@PathVariable String id) {
		UserEntity user = _userAppService.getUser();

		FindReportuserByIdOutput reportuser = _reportuserAppService.findById(new ReportuserId(Long.valueOf(id), user.getId()));
		Optional.ofNullable(reportuser).orElseThrow(() -> new EntityNotFoundException(String.format("No Report is shared with id=%s", id)));

		ReportDetailsOutput output = _reportAppService.resetReport(user.getId(), Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Report can not be resetted")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

}


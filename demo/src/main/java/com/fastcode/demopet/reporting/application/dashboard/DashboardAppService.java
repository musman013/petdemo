package com.fastcode.demopet.reporting.application.dashboard;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.authorization.userrole.IUserroleManager;
import com.fastcode.demopet.domain.model.DashboardEntity;
import com.fastcode.demopet.domain.model.DashboardroleEntity;
import com.fastcode.demopet.domain.model.DashboardroleId;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionId;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportId;
import com.fastcode.demopet.domain.model.QDashboardEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.reporting.application.dashboard.dto.*;
import com.fastcode.demopet.reporting.application.dashboardrole.DashboardroleAppService;
import com.fastcode.demopet.reporting.application.dashboardrole.dto.CreateDashboardroleInput;
import com.fastcode.demopet.reporting.application.dashboarduser.DashboarduserAppService;
import com.fastcode.demopet.reporting.application.dashboarduser.dto.CreateDashboarduserInput;
import com.fastcode.demopet.reporting.application.dashboardversion.DashboardversionAppService;
import com.fastcode.demopet.reporting.application.dashboardversion.IDashboardversionMapper;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.CreateDashboardversionInput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.CreateDashboardversionOutput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.UpdateDashboardversionInput;
import com.fastcode.demopet.reporting.application.dashboardversion.dto.UpdateDashboardversionOutput;
import com.fastcode.demopet.reporting.application.dashboardversionreport.DashboardversionreportAppService;
import com.fastcode.demopet.reporting.application.report.ReportAppService;
import com.fastcode.demopet.reporting.application.report.ReportMapper;
import com.fastcode.demopet.reporting.application.report.dto.CreateReportInput;
import com.fastcode.demopet.reporting.application.report.dto.CreateReportOutput;
import com.fastcode.demopet.reporting.application.report.dto.FindReportByIdOutput;
import com.fastcode.demopet.reporting.application.report.dto.ReportDetailsOutput;
import com.fastcode.demopet.reporting.application.report.dto.ShareReportInput;
import com.fastcode.demopet.reporting.application.report.dto.UpdateReportInput;
import com.fastcode.demopet.reporting.application.reportrole.dto.CreateReportroleInput;
import com.fastcode.demopet.reporting.domain.dashboard.DashboardManager;
import com.fastcode.demopet.reporting.domain.dashboard.IDashboardManager;
import com.fastcode.demopet.reporting.domain.dashboardrole.IDashboardroleManager;
import com.fastcode.demopet.reporting.domain.dashboarduser.IDashboarduserManager;
import com.fastcode.demopet.reporting.domain.dashboardversion.IDashboardversionManager;
import com.fastcode.demopet.reporting.domain.dashboardversionreport.IDashboardversionreportManager;
import com.fastcode.demopet.reporting.domain.report.IReportManager;
import com.fastcode.demopet.reporting.domain.reportuser.IReportuserManager;
import com.fastcode.demopet.reporting.domain.reportversion.IReportversionManager;
import com.google.common.collect.Sets.SetView;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import java.util.stream.Collectors;

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
public class DashboardAppService implements IDashboardAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private IDashboardManager _dashboardManager;

	@Autowired
	private DashboardversionreportAppService _reportDashboardAppService;

	@Autowired
	private ReportAppService _reportAppService;

	@Autowired
	private DashboardversionAppService _dashboardversionAppservice;

	@Autowired
	private IDashboardversionMapper dashboardversionMapper;

	@Autowired
	private DashboardroleAppService _dashboardroleAppservice;

	@Autowired
	private DashboarduserAppService _dashboarduserAppservice;

	@Autowired
	private IDashboardversionManager _dashboardversionManager;

	@Autowired
	private IReportversionManager _reportversionManager;

	@Autowired
	private IDashboardversionreportManager _reportDashboardManager;

	@Autowired
	private IDashboarduserManager _dashboarduserManager;

	@Autowired
	private IReportuserManager _reportuserManager;

	@Autowired
	private IDashboardroleManager _dashboardroleManager;

	@Autowired
	private IUserroleManager _userroleManager;

	@Autowired
	private UserManager _userManager;

	@Autowired
	private DashboardMapper mapper;

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private IReportManager _reportManager;

	@Autowired
	private LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateDashboardOutput create(CreateDashboardInput input) {

		DashboardEntity dashboard = mapper.createDashboardInputToDashboardEntity(input);
		if(input.getOwnerId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getOwnerId());
			if(foundUser!=null) {
				//	dashboard.setUser(foundUser);
				foundUser.addDashboard(dashboard);
			}
		}

		DashboardEntity createdDashboard = _dashboardManager.create(dashboard);
		CreateDashboardversionInput dashboardversion = mapper.creatDashboardInputToCreateDashboardversionInput(input);
		dashboardversion.setDashboardId(createdDashboard.getId());

		CreateDashboardversionOutput dashboardversionOutput = _dashboardversionAppservice.create(dashboardversion);

		return mapper.dashboardEntityAndCreateDashboardversionOutputToCreateDashboardOutput(createdDashboard,dashboardversionOutput);
	}

//	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Dashboard", key = "#p0")
	public UpdateDashboardOutput update(Long  dashboardId, UpdateDashboardInput input) {

		DashboardversionId dashboardversionId = new DashboardversionId(input.getUserId(), dashboardId, "running");

		DashboardversionEntity rv = _dashboardversionManager.findById(dashboardversionId);
		UpdateDashboardversionInput dashboardversion = mapper.updateDashboardInputToUpdateDashboardversionInput(input);
		dashboardversion.setDashboardId(rv.getDashboard().getId());
		dashboardversion.setDashboardVersion(rv.getDashboardVersion());
		dashboardversion.setVersion(rv.getVersion());

	//	dashboardversion.setVersion(rv.getVersion());
		UpdateDashboardversionOutput dashboardversionOutput =  _dashboardversionAppservice.update(dashboardversionId, dashboardversion);

		DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, input.getUserId()));
		if(dashboarduser !=null)
		{
			dashboarduser.setIsResetted(false);
			_dashboarduserManager.update(dashboarduser);
		}

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		if(foundDashboard.getUser() !=null && foundDashboard.getUser().getId() == input.getUserId())
		{
			foundDashboard.setIsPublished(false);
			_dashboardManager.update(foundDashboard);
		}

		Long count =0L;
		for(UpdateReportInput reportInput : input.getReportDetails()) {
			DashboardversionreportEntity dashboardreport = _reportDashboardManager.findById(new DashboardversionreportId(dashboardId, input.getUserId(),"running", reportInput.getId()));
			if(reportInput.getReportWidth() !=null) {
				dashboardreport.setReportWidth(reportInput.getReportWidth());
			}
			else {
				dashboardreport.setReportWidth("mediumchart");
			}
			dashboardreport.setOrderId(count);
			count ++;

			DashboardversionreportEntity e = _reportDashboardManager.update(dashboardreport);
		}

		return mapper.dashboardEntityAndUpdateDashboardversionOutputToUpdateDashboardOutput(foundDashboard,dashboardversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Dashboard", key = "#p0")
	public void delete(Long dashboardId, Long userId) {

		DashboardEntity existing = _dashboardManager.findById(dashboardId) ; 
		existing.removeDashboardversion(_dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running")));
		existing.removeDashboardversion(_dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "published")));
		_dashboardversionAppservice.delete(new DashboardversionId(userId, dashboardId, "running"));
		_dashboardversionAppservice.delete(new DashboardversionId(userId, dashboardId, "published"));

		List<DashboarduserEntity> reportUserList = _dashboarduserManager.findByDashboardId(existing.getId());
		for(DashboarduserEntity reportuser : reportUserList)
		{
			reportuser.setOwnerSharingStatus(false);
			_dashboarduserManager.update(reportuser);
		}

		existing.setUser(null);
		existing.setIsPublished(true);
		_dashboardManager.update(existing);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Dashboard", key = "#p0")
	public void deleteReportFromDashboard(Long dashboardId, Long reportId, Long userId) {

		DashboardversionEntity dashboardVersion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running"));
		dashboardVersion.removeDashboardversionreport(_reportDashboardManager.findById(new DashboardversionreportId(dashboardId, userId, "running", reportId)));
		
	//	_reportDashboardAppService.delete(new DashboardversionreportId(dashboardId, userId, "running", reportId));
		
		List<DashboardversionreportEntity> dashboardReportList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "running", userId);

		Boolean sharable = true;
		for (DashboardversionreportEntity dashboardreport : dashboardReportList)
		{
			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(dashboardreport.getReportId(), userId));
			if(reportuser != null)
				sharable = false;
		}

		if(sharable) {
			DashboardEntity dashboard = _dashboardManager.findByDashboardIdAndUserId(dashboardId, userId);
			dashboard.setIsShareable(sharable);
			dashboard.setIsPublished(false);
			_dashboardManager.update(dashboard);
		}


	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public FindDashboardByIdOutput findById(Long dashboardId) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		if (foundDashboard == null)  
			return null ; 

		FindDashboardByIdOutput output=mapper.dashboardEntityToFindDashboardByIdOutput(foundDashboard); 
		return output;
	}

	public List<FindReportByIdOutput> setReportsList(Long dashboardId, Long userId)
	{
		List<DashboardversionreportEntity> reportDashboardList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId,"running",userId);

		List<FindReportByIdOutput> reportDetails = new ArrayList<>();
		for(DashboardversionreportEntity rd : reportDashboardList)
		{
			ReportversionEntity reportversion = _reportversionManager.findById(new ReportversionId(rd.getUserId(), rd.getReportId(), "running"));
			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(rd.getReportId(), rd.getUserId()));
			FindReportByIdOutput output= reportMapper.reportEntitiesToFindReportByIdOutput(rd.getReport(), reportversion, reportuser); 

			if(reportuser !=null)
			{
				output.setSharedWithMe(true);
			}

			List<ReportuserEntity> reportuserList = _reportuserManager.findByReportId(rd.getReportId());
			if(reportuserList !=null && !reportuserList.isEmpty()) {
				output.setSharedWithOthers(true);
			}

			output.setOrderId(rd.getOrderId());
			output.setReportWidth(rd.getReportWidth());
			reportDetails.add(output);
		}

		List<FindReportByIdOutput>  sortedReports = reportDetails.stream()
				.sorted(Comparator.comparing(FindReportByIdOutput::getOrderId))
				.collect(Collectors.toList());

		return sortedReports;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public FindDashboardByIdOutput findByDashboardIdAndUserId(Long dashboardId, Long userId) {

		DashboardEntity foundDashboard = _dashboardManager.findByDashboardIdAndUserId(dashboardId, userId);
		DashboarduserEntity dashboarduser =  _dashboarduserManager.findById(new DashboarduserId(dashboardId, userId));

		if (foundDashboard == null && dashboarduser == null)  
			return null ; 

		DashboardversionEntity dashboardVersion =_dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running"));
		FindDashboardByIdOutput output = mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,dashboardVersion, dashboarduser); 

		DashboardversionEntity publishedversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "published"));
		if(dashboarduser != null) {
			output.setSharedWithMe(true);
		}

		List<DashboarduserEntity> dashboarduserList = _dashboarduserManager.findByDashboardId(dashboardId);
		if(dashboarduserList !=null && !dashboarduserList.isEmpty())
		{
			output.setSharedWithOthers(true);
		}

		if(publishedversion == null)
		{
			output.setIsResetable(false);
		}
		else 
			output.setIsResetable(true);

		return output;

	}

	//User
	// ReST API Call - GET /dashboard/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Dashboard", key="#p0")
	public GetUserOutput getUser(Long dashboardId) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		if (foundDashboard == null) {
			logHelper.getLogger().error("There does not exist a dashboard wth a id=%s", dashboardId);
			return null;
		}
		UserEntity re = _dashboardManager.getUser(dashboardId);
		return mapper.userEntityToGetUserOutput(re, foundDashboard);
	}

	//User
	// ReST API Call - GET /dashboard/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//		@Cacheable (value = "Dashboard", key="#p0")
	public List<GetUserOutput> getUsersAssociatedWithDashboard(Long dashboardId, String search, Pageable pageable) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		Page<UserEntity> foundUser = _dashboarduserManager.getDashboardUsersList(dashboardId,search,pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator();
		List<GetUserOutput> usersList = new ArrayList<>();

		while (userIterator.hasNext()) {
			UserEntity user = userIterator.next();DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, user.getId()));
			GetUserOutput output = mapper.userEntityToGetUserOutput(user, foundDashboard);
			if(dashboarduser != null)
			{
				output.setEditable(dashboarduser.getEditable());
			}
			usersList.add(output);
		}

		return usersList;
	}

	//User
	// ReST API Call - GET /dashboard/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Dashboard", key="#p0")
	public List<GetUserOutput> getAvailableUsers(Long dashboardId, String search, Pageable pageable) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		Page<UserEntity> foundUser = _dashboarduserManager.getAvailableUsersList(dashboardId,search,pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator();
		List<GetUserOutput> usersList = new ArrayList<>();

		while (userIterator.hasNext()) {
			UserEntity user = userIterator.next();
			GetUserOutput output = mapper.userEntityToGetUserOutput(user, foundDashboard);
			usersList.add(output);
		}

		return usersList;
	}

	//Role
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Dashboard", key="#p0")
	public List<GetRoleOutput> getRolesAssociatedWithDashboard(Long dashboardId, String search, Pageable pageable) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		Page<RoleEntity> foundRole = _dashboardroleManager.getDashboardRolesList(dashboardId,search,pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator();
		List<GetRoleOutput> rolesList = new ArrayList<>();

		while (roleIterator.hasNext()) {
			RoleEntity role = roleIterator.next();
			DashboardroleEntity dashboardrole = _dashboardroleManager.findById(new DashboardroleId(dashboardId, role.getId()));
			GetRoleOutput output = mapper.roleEntityToGetRoleOutput(role, foundDashboard);
			if(dashboardrole != null)
			{
				output.setEditable(dashboardrole.getEditable());
			}
			rolesList.add(output);
		}

		return rolesList;
	}

	//User
	// ReST API Call - GET /dashboard/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Dashboard", key="#p0")
	public List<GetRoleOutput> getAvailableRoles(Long dashboardId, String search, Pageable pageable) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		Page<RoleEntity> foundRole = _dashboardroleManager.getAvailableRolesList(dashboardId,search,pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator();
		List<GetRoleOutput> rolesList = new ArrayList<>();

		while (roleIterator.hasNext()) {
			RoleEntity role = roleIterator.next();
			GetRoleOutput output = mapper.roleEntityToGetRoleOutput(role, foundDashboard);
			rolesList.add(output);
		}

		return rolesList;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public DashboardDetailsOutput shareDashboard(Long dashboardId, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {
		DashboardEntity dashboard = _dashboardManager.findById(dashboardId);
		DashboardversionEntity ownerPublishedVersion = _dashboardversionManager.findById(new DashboardversionId(dashboard.getUser().getId(), dashboard.getId(), "published"));
		if(ownerPublishedVersion==null)
		{
			return null;
		}
		List<DashboardversionreportEntity> dashboardReportsList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "published", dashboard.getUser().getId());
		List<ShareReportInput> reportUsers = new ArrayList<ShareReportInput>();
		List<Long> usersWithSharedReportsByRole = new ArrayList<>();
		for(ShareReportInput roleInput : rolesList)
		{
			DashboardroleEntity dashboardrole = _dashboardroleManager.findById(new DashboardroleId(dashboardId, roleInput.getId()));
			if(dashboardrole == null) {
				CreateDashboardroleInput dashboardRoleInput = new CreateDashboardroleInput();
				dashboardRoleInput.setRoleId(roleInput.getId());
				dashboardRoleInput.setDashboardId(dashboardId);
				dashboardRoleInput.setEditable(roleInput.getEditable());
				dashboardRoleInput.setOwnerSharingStatus(true);
				_dashboardroleAppservice.create(dashboardRoleInput);
			}
			else if (dashboardrole !=null && !dashboardrole.getOwnerSharingStatus())
			{
				dashboardrole.setOwnerSharingStatus(true);
				dashboardrole.setEditable(roleInput.getEditable());
				_dashboardroleManager.update(dashboardrole);
			}


			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
			for(UserroleEntity userrole : userroleList)
			{
				usersWithSharedReportsByRole.add(userrole.getUserId());
				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userrole.getUserId()));

				if(dashboarduser != null ) {
					if(!dashboard.getUser().getId().equals(dashboarduser.getUser().getId())) {
						shareDashboardWithUser(dashboarduser,ownerPublishedVersion, roleInput.getEditable());
						dashboarduser.setEditable(roleInput.getEditable());
						dashboarduser.setIsAssignedByRole(true);
						_dashboarduserManager.update(dashboarduser);
					}
				}

				else {
					createDashboarduserAndDashboardVersion(ownerPublishedVersion,userrole.getUserId(), roleInput.getEditable(),true);

				}

				ShareReportInput reportInput = new ShareReportInput();
				reportInput.setId(userrole.getUserId());
				reportInput.setEditable(roleInput.getEditable());
				reportUsers.add(reportInput);
			}
		}

		for(ShareReportInput userInput : usersList)
		{
			if(!usersWithSharedReportsByRole.contains(userInput.getId())) {

				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userInput.getId()));

				if(dashboarduser !=null ) {
					if(!dashboard.getUser().getId().equals(dashboarduser.getUser().getId())) {
						shareDashboardWithUser(dashboarduser,ownerPublishedVersion, userInput.getEditable());
						dashboarduser.setEditable(userInput.getEditable());
						dashboarduser.setIsAssignedByRole(false);
						_dashboarduserManager.update(dashboarduser);
					}
				}

				else {
					createDashboarduserAndDashboardVersion(ownerPublishedVersion,userInput.getId(),userInput.getEditable(),false);
				}

				ShareReportInput reportInput = new ShareReportInput();
				reportInput.setId(userInput.getId());
				reportInput.setEditable(userInput.getEditable());
				reportUsers.add(reportInput);
			}
		}

		for(DashboardversionreportEntity dvr :dashboardReportsList)
		{
			_reportAppService.shareReport(dvr.getReportId(),true, usersList, rolesList);

			for(ShareReportInput reportuser : reportUsers) {
				DashboardversionreportEntity running = _reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), reportuser.getId(), "running", dvr.getReportId()));

				if(running == null) {
					running = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, reportuser.getId(), "running");
					_reportDashboardManager.create(running);
				}
				if(reportuser.getEditable()) {
					DashboardversionreportEntity published = _reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), reportuser.getId(), "published", dvr.getReportId()));

					if(published == null) {
						published = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(running, reportuser.getId(), "published");
						_reportDashboardManager.create(published);
					}
				}

			}
		}

		DashboardDetailsOutput dashboardDetails = mapper.dashboardEntitiesToDashboardDetailsOutput(dashboard, ownerPublishedVersion, null);
		dashboardDetails.setSharedWithOthers(true);
		return dashboardDetails;
	}


	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public DashboardDetailsOutput editDashboardAccess(Long dashboardId, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {

		DashboardEntity dashboard = _dashboardManager.findById(dashboardId);
		DashboardversionEntity ownerPublishedVersion = _dashboardversionManager.findById(new DashboardversionId(dashboard.getUser().getId(), dashboard.getId(), "published"));
		if(ownerPublishedVersion==null)
		{
			return null;
		}

		List<DashboardversionreportEntity> dashboardReportsList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "published", dashboard.getUser().getId());
		List<ShareReportInput> reportUsers = new ArrayList<ShareReportInput>();
		List<Long> usersWithSharedDashboardsByRole = new ArrayList<>();
		for(ShareReportInput roleInput : rolesList)
		{
			DashboardroleEntity dashboardRole= _dashboardroleManager.findById(new DashboardroleId(dashboardId, roleInput.getId()));
			if(roleInput.getEditable() !=null)
			{
				dashboardRole.setEditable(roleInput.getEditable());
				dashboardRole.setOwnerSharingStatus(true);
				_dashboardroleManager.update(dashboardRole);
			}
			else 
			{
				dashboardRole.setOwnerSharingStatus(false);
				_dashboardroleManager.update(dashboardRole);
			}

			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
			for(UserroleEntity userrole : userroleList)
			{
				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userrole.getUserId()));

				if(!dashboarduser.getUser().getId().equals(userrole.getUserId())) {
					usersWithSharedDashboardsByRole.add(userrole.getUserId());

					if(dashboarduser.getIsAssignedByRole())
					{
						if(dashboarduser !=null && roleInput.getEditable() !=null) {

							shareDashboardWithUser(dashboarduser,ownerPublishedVersion, roleInput.getEditable());
							dashboarduser.setEditable(roleInput.getEditable());
							dashboarduser.setIsAssignedByRole(true);
							_dashboarduserManager.update(dashboarduser);

						}
						else if (roleInput.getEditable() == null && dashboarduser !=null) {

							dashboarduser.setOwnerSharingStatus(false);
							_dashboarduserManager.update(dashboarduser);
						}

						ShareReportInput reportInput = new ShareReportInput();
						reportInput.setId(userrole.getUserId());
						reportInput.setEditable(roleInput.getEditable());
						reportUsers.add(reportInput);
					}
				}
			}

		}

		for(ShareReportInput userInput : usersList)
		{
			if(!usersWithSharedDashboardsByRole.contains(userInput.getId())) {
				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userInput.getId()));

				if(dashboarduser !=null && !dashboarduser.getIsAssignedByRole())
				{
					if(dashboarduser !=null && userInput.getEditable() != null) {
						shareDashboardWithUser(dashboarduser,ownerPublishedVersion, userInput.getEditable());
						dashboarduser.setEditable(userInput.getEditable());
						dashboarduser.setIsAssignedByRole(false);
						_dashboarduserManager.update(dashboarduser);

					}
					else if (userInput.getEditable() == null && dashboarduser !=null) {
						dashboarduser.setOwnerSharingStatus(false);
						dashboarduser = _dashboarduserManager.update(dashboarduser);
					}

					ShareReportInput reportInput = new ShareReportInput();
					reportInput.setId(userInput.getId());
					reportInput.setEditable(userInput.getEditable());
					reportUsers.add(reportInput);
				}

			}
		}


		for(DashboardversionreportEntity dvr :dashboardReportsList)
		{
			_reportAppService.editReportAccess(dvr.getReportId(), usersList, rolesList);

			for(ShareReportInput reportuser : reportUsers) {

				if(reportuser.getEditable() !=null) {
					DashboardversionreportEntity running = _reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), reportuser.getId(), "running", dvr.getReportId()));
					if(running == null) {
						running = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, reportuser.getId(), "published");
						_reportDashboardManager.create(running);
					}

					if(reportuser.getEditable()) {
						DashboardversionreportEntity published = _reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), reportuser.getId(), "published", dvr.getReportId()));

						if(published ==null) {
							published = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(running,reportuser.getId(), "published");
						}

						_reportDashboardManager.create(published);
					}
				}
			}
		}


		DashboardDetailsOutput dashboardDetails = mapper.dashboardEntitiesToDashboardDetailsOutput(dashboard, ownerPublishedVersion, null);
		return dashboardDetails;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createDashboarduserAndDashboardVersion(DashboardversionEntity ownerReportversion, Long userId, Boolean editable, Boolean isAssigByRole)
	{

		CreateDashboarduserInput createDashboarduserInput = new CreateDashboarduserInput();
		createDashboarduserInput.setDashboardId(ownerReportversion.getDashboardId());
		createDashboarduserInput.setUserId(userId);
		createDashboarduserInput.setEditable(editable);
		createDashboarduserInput.setIsAssignedByRole(isAssigByRole);
		createDashboarduserInput.setIsResetted(true);
		createDashboarduserInput.setIsRefreshed(true);
		createDashboarduserInput.setOwnerSharingStatus(true);
		createDashboarduserInput.setRecipientSharingStatus(true);
		_dashboarduserAppservice.create(createDashboarduserInput);

		UserEntity user = _userManager.findById(userId);
		if(editable) {
			DashboardversionEntity publishedDashboardversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerReportversion, user.getId(), "published"); 
			publishedDashboardversion.setUser(user);
			_dashboardversionManager.create(publishedDashboardversion);
			DashboardversionEntity runningDashboardversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerReportversion, user.getId(), "running"); 
			runningDashboardversion.setUser(user);
			_dashboardversionManager.create(runningDashboardversion);
		}
		else {
			DashboardversionEntity runningdashboardversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerReportversion, user.getId(), "running"); 

			runningdashboardversion.setUser(user);
			_dashboardversionManager.create(runningdashboardversion);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void shareDashboardWithUser(DashboarduserEntity dashboarduser, DashboardversionEntity ownerPublishedVersion, Boolean editable)
	{
		UserEntity user = _userManager.findById(dashboarduser.getUserId());

		DashboardversionEntity dashboardPublishedVersion = _dashboardversionManager.findById(new DashboardversionId(user.getId(),dashboarduser.getDashboardId(),"published"));
		DashboardversionEntity dashboardRunningVersion = _dashboardversionManager.findById(new DashboardversionId(user.getId(),dashboarduser.getDashboardId(),"running"));

		if(dashboarduser.getEditable() && !editable) {

			if(dashboarduser.getOwnerSharingStatus()) {
				if(dashboarduser.getIsResetted()) {
					if (dashboardPublishedVersion != null) {
					   _dashboardversionManager.delete(dashboardPublishedVersion);	
					}

					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "running"); 
					publishedVersion.setUser(user);
					_dashboardversionManager.update(publishedVersion);
				}
				else if(!dashboarduser.getIsResetted()) {
					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "published"); 

					publishedVersion.setUser(user);
					_dashboardversionManager.update(publishedVersion);
				}
			}
			else
			{
				if(dashboarduser.getIsResetted()) {
					if (dashboardPublishedVersion != null) {
						_dashboardversionManager.delete(dashboardPublishedVersion);	
					}
				}
			}


		} else if(!dashboarduser.getEditable() && !editable) {
			if(dashboarduser.getOwnerSharingStatus()) {
				if(dashboardPublishedVersion !=null && !dashboarduser.getIsResetted()) {
					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "published"); 
					publishedVersion.setUser(user);
					_dashboardversionManager.update(publishedVersion);
				}
				else if(dashboarduser.getIsResetted()) {
					if (dashboardPublishedVersion != null) {
						_dashboardversionManager.delete(dashboardPublishedVersion);	
					}
					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "running"); 

					publishedVersion.setUser(user);
					_dashboardversionManager.update(publishedVersion);
				}
			}


		} else if(dashboarduser.getEditable() && editable) {
			if(dashboarduser.getOwnerSharingStatus()) {
				DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_dashboardversionManager.update(publishedVersion);
			}

		} else if(!dashboarduser.getEditable() && editable) {
			if(dashboarduser.getOwnerSharingStatus()) {
				if(dashboardPublishedVersion !=null && !dashboarduser.getIsResetted()) {
					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "published"); 
					publishedVersion.setUser(user);
					_dashboardversionManager.update(publishedVersion);
				}
				else if(dashboarduser.getIsResetted()) {

					if (dashboardPublishedVersion != null) {
						_dashboardversionManager.delete(dashboardPublishedVersion);	
					}

					DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedVersion, user.getId(), "published"); 
					publishedVersion.setUser(user);
					_dashboardversionManager.create(publishedVersion);
				}
			}
			else
			{
				DashboardversionEntity publishedVersion =dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(dashboardRunningVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_dashboardversionManager.create(publishedVersion);
			}

		}

		if(!dashboarduser.getOwnerSharingStatus()) {
			dashboarduser.setOwnerSharingStatus(true);
			dashboarduser.setIsRefreshed(false);
			_dashboarduserManager.update(dashboarduser);	
		}
	}

	//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	//	public DashboardDetailsOutput unshareDashboard(Long dashboardId, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {
	//		DashboardEntity dashboard = _dashboardManager.findById(dashboardId);
	//		DashboardversionEntity ownerPublishedVersion = _dashboardversionManager.findById(new DashboardversionId(dashboard.getUser().getId(), dashboardId, "published"));
	//		if(ownerPublishedVersion==null)
	//		{
	//			return null;
	//		}
	//		List<Long> usersWithSharedDashboardsByRole = new ArrayList<>();
	//		for(ShareReportInput roleInput : rolesList)
	//		{	
	//			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
	//			for(UserroleEntity userrole : userroleList)
	//			{
	//				usersWithSharedDashboardsByRole.add(userrole.getUserId());
	//				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userrole.getUserId()));
	//
	//				if(dashboarduser !=null ) {
	//					dashboarduser.setOwnerSharingStatus(false);
	//					dashboarduser = _dashboarduserManager.update(dashboarduser);
	//				}
	//			}
	//
	//		}
	//
	//		for(ShareReportInput userInput : usersList)
	//		{
	//			if(!usersWithSharedDashboardsByRole.contains(userInput.getId())) {
	//
	//				DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userInput.getId()));
	//
	//				if(dashboarduser != null ) {
	//					dashboarduser.setOwnerSharingStatus(false);
	//					dashboarduser = _dashboarduserManager.update(dashboarduser);
	//				}
	//			}
	//		}
	//
	//		DashboardDetailsOutput dashboardDetails = mapper.dashboardEntitiesToDashboardDetailsOutput(dashboard, ownerPublishedVersion, null);
	//		dashboardDetails.setSharedWithOthers(false);
	//		return dashboardDetails;
	//	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<DashboardDetailsOutput> getDashboards(Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<DashboardDetailsOutput> foundDashboard = _dashboardManager.getDashboards(userId, search, pageable) ;
		List<DashboardDetailsOutput> dashboardList = foundDashboard.getContent();

		return dashboardList ;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<DashboardDetailsOutput> getAvailableDashboards(Long userId, Long reportId, String search, Pageable pageable) throws Exception
	{ 
		Page<DashboardDetailsOutput> foundDashboard = _dashboardManager.getAvailableDashboards(userId, reportId, search, pageable) ;
		List<DashboardDetailsOutput> dashboardList = foundDashboard.getContent();

		return dashboardList ;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<DashboardDetailsOutput> getSharedDashboards(Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<DashboardDetailsOutput> foundDashboard = _dashboardManager.getSharedDashboards(userId, search, pageable) ;
		List<DashboardDetailsOutput> dashboardList = foundDashboard.getContent();

		return dashboardList ;
	}


	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public DashboardDetailsOutput updateRecipientSharingStatus (Long userId, Long dashboardId, Boolean status) {

		DashboarduserEntity foundDashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userId));
		if(foundDashboarduser ==null)
			return null;
		foundDashboarduser.setRecipientSharingStatus(status);
		foundDashboarduser = _dashboarduserManager.update(foundDashboarduser);

		DashboardversionEntity foundDashboardversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running"));
		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);

		return mapper.dashboardEntitiesToDashboardDetailsOutput(foundDashboard,foundDashboardversion,foundDashboarduser);

	}

	//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public FindDashboardByIdOutput publishDashboard(Long userId, Long dashboardId) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);

		List<DashboarduserEntity> dashboarduserList = _dashboarduserManager.findByDashboardId(dashboardId);
		for(DashboarduserEntity dashboarduser: dashboarduserList)
		{
			dashboarduser.setIsRefreshed(false);
			_dashboarduserManager.update(dashboarduser);
		}

		foundDashboard.setIsPublished(true);
		foundDashboard = _dashboardManager.update(foundDashboard);

		DashboardversionEntity foundDashboardversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running"));
		DashboardversionEntity foundPublishedversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "published"));
		DashboardversionEntity publishedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(foundDashboardversion, userId, "published");
		
		if(foundPublishedversion !=null)
		{
			publishedVersion.setVersion(foundPublishedversion.getVersion());
		}
		else
			publishedVersion.setVersion(0L);
		
		_dashboardversionManager.update(publishedVersion);

		//check if report is added in running version
		List<DashboardversionreportEntity> dashboardreportList  = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "running", userId);

		for(DashboardversionreportEntity dashboardreport : dashboardreportList)
		{
			DashboardversionreportEntity publishedDashboardreport = _reportDashboardManager.findById(new DashboardversionreportId(dashboardId,userId,"published", dashboardreport.getReportId()));
			if(publishedDashboardreport !=null) {
				publishedDashboardreport.setOrderId(dashboardreport.getOrderId());
				publishedDashboardreport.setReportWidth(dashboardreport.getReportWidth());
				_reportDashboardManager.update(publishedDashboardreport);

			}
			else
			{
				publishedDashboardreport = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dashboardreport, userId, "published");
				_reportDashboardManager.create(publishedDashboardreport);
			}

			_reportAppService.publishReport(dashboardreport.getUserId(), dashboardreport.getReportId());
		}

		//check if report is removed from running version
		List<DashboardversionreportEntity> dashboardPublishedreportList  = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "published", userId);

		for(DashboardversionreportEntity dashboardeport : dashboardPublishedreportList)
		{
			DashboardversionreportEntity runningDashboardreport = _reportDashboardManager.findById(new DashboardversionreportId(dashboardId,userId,"running", dashboardeport.getReportId()));
			if(runningDashboardreport == null) {

				runningDashboardreport = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dashboardeport, userId, "published");
				_reportDashboardManager.delete(runningDashboardreport);
				ReportversionEntity reportversion = _reportversionManager.findById(new ReportversionId(dashboardeport.getUserId(), dashboardeport.getUserId(), "running"));
				if(reportversion.getIsAssignedByDashboard())
				{
					_reportAppService.delete(dashboardeport.getReportId(), dashboardeport.getUserId());
				}
			}
		}

		return mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,foundDashboardversion,null);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public DashboardDetailsOutput changeOwner(Long ownerId, Long dashboardId, Long newOwnerId) {

		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);
		UserEntity foundUser = _userManager.findById(newOwnerId);

		DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, newOwnerId));
		DashboardversionEntity dashboardRunningversion;
		if(dashboarduser != null) {

			dashboardRunningversion = _dashboardversionManager.findById(new DashboardversionId(newOwnerId, dashboardId, "running"));
			DashboardversionEntity dashboardPublishedversion = _dashboardversionManager.findById(new DashboardversionId(newOwnerId, dashboardId, "published"));

			if(dashboardPublishedversion == null) {
				dashboardPublishedversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(dashboardRunningversion, newOwnerId, "published");
				dashboardPublishedversion.setUser(foundUser);
				_dashboardversionManager.create(dashboardPublishedversion);
			}

			_dashboarduserManager.delete(dashboarduser);
		}
		else {
			DashboardversionEntity foundOwnerDashboardRunningversion = _dashboardversionManager.findById(new DashboardversionId(ownerId, dashboardId, "running"));
			DashboardversionEntity foundOwnerDashboardPublishedversion = _dashboardversionManager.findById(new DashboardversionId(ownerId, dashboardId, "published"));
			dashboardRunningversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(foundOwnerDashboardRunningversion, foundUser.getId(), "running");
			dashboardRunningversion.setUser(foundUser);
			_dashboardversionManager.create(dashboardRunningversion);

			DashboardversionEntity dashboardPublishedversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(foundOwnerDashboardPublishedversion, foundUser.getId(), "published");

			dashboardPublishedversion.setUser(foundUser);
			_dashboardversionManager.create(dashboardPublishedversion);
		}


		List<DashboardversionreportEntity> dvrRunningList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId,"running", ownerId);
		List<DashboardversionreportEntity> dvrPublishedList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId,"published", ownerId);

		for(DashboardversionreportEntity dvr : dvrRunningList)
		{
			_reportAppService.changeOwner(ownerId, dvr.getReportId(), newOwnerId);	
			ReportuserEntity ru = _reportuserManager.findById(new ReportuserId(dvr.getReportId(), ownerId));
			if(ru == null) {
				DashboardversionreportEntity updatedRunning = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, newOwnerId, "running");
				_reportDashboardManager.create(updatedRunning);
			}

			_reportDashboardManager.delete(dvr);

			DashboardversionreportEntity ownerPublished=_reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), dvr.getUserId(), "published", dvr.getReportId()));
			if(ownerPublished !=null) 
				_reportDashboardManager.delete(ownerPublished);
		}

		for(DashboardversionreportEntity dvr : dvrPublishedList) {

			if(!dvrRunningList.stream().filter(o -> o.getReportId().equals(dvr.getReportId())).findFirst().isPresent()) {

				_reportAppService.changeOwner(ownerId, dvr.getReportId(), newOwnerId);	
				ReportuserEntity ru = _reportuserManager.findById(new ReportuserId(dvr.getReportId(), ownerId));
				if(ru == null) {
					DashboardversionreportEntity updatedPublished = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, newOwnerId, "published");
					_reportDashboardManager.create(updatedPublished);
				}

				_reportDashboardManager.delete(dvr);
			}           

			ReportuserEntity ru = _reportuserManager.findById(new ReportuserId(dvr.getReportId(), ownerId));
			if(ru == null) {
				DashboardversionreportEntity updatedPublished = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, newOwnerId, "published");
				_reportDashboardManager.create(updatedPublished);
			}

		}


		_dashboardversionAppservice.delete(new DashboardversionId(ownerId, dashboardId, "running"));
		_dashboardversionAppservice.delete(new DashboardversionId(ownerId, dashboardId, "published"));

		foundDashboard.setUser(foundUser);
		_dashboardManager.update(foundDashboard);

		return mapper.dashboardEntitiesToDashboardDetailsOutput(foundDashboard,dashboardRunningversion,null);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public FindDashboardByIdOutput refreshDashboard(Long userId, Long dashboardId) {

		DashboarduserEntity foundDashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userId));
		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);

		if(foundDashboarduser !=null && foundDashboarduser.getOwnerSharingStatus()) {

			DashboardversionEntity ownerPublishedversion = _dashboardversionManager.findById(new DashboardversionId(foundDashboard.getUser().getId(), dashboardId, "published"));
			UserEntity foundUser = _userManager.findById(userId);

			DashboardversionEntity publishedversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "published"));
			DashboardversionEntity updatedVersion;
			if(publishedversion == null)
			{
				updatedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(ownerPublishedversion, userId, "published"); 
			}
			else
			{
				updatedVersion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(publishedversion, userId, "running"); 
			}

			updatedVersion.setUser(foundUser);
			_dashboardversionManager.update(updatedVersion);
			foundDashboarduser.setIsRefreshed(true);
			foundDashboarduser.setIsResetted(false);
			foundDashboarduser = _dashboarduserManager.update(foundDashboarduser);

			List<DashboardversionreportEntity> dvrList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "published", foundDashboard.getUser().getId());
			for(DashboardversionreportEntity dvr : dvrList)
			{
				DashboardversionreportEntity updateDashboardreport = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, userId, "published");
				_reportDashboardManager.update(updateDashboardreport);
				_reportAppService.refreshReport(userId,dvr.getReportId());

			}

			DashboardversionEntity runningversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "running"));

			FindDashboardByIdOutput output = mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,runningversion,foundDashboarduser);
			output.setSharedWithMe(true);
			return output;

		}

		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Dashboard", key = "#p0")
	public FindDashboardByIdOutput resetDashboard(Long userId, Long dashboardId) {

		DashboarduserEntity foundDashboarduser = _dashboarduserManager.findById(new DashboarduserId(dashboardId, userId));
		DashboardEntity foundDashboard = _dashboardManager.findById(dashboardId);

		DashboardversionEntity publishedversion = _dashboardversionManager.findById(new DashboardversionId(userId, dashboardId, "published"));
		if(publishedversion !=null)
		{
			DashboardversionEntity runningversion = dashboardversionMapper.dashboardversionEntityToDashboardversionEntity(publishedversion, userId, "running");
			//	runningversion.setVersion("running");
			runningversion = _dashboardversionManager.update(runningversion);


			foundDashboarduser.setIsResetted(true);
			foundDashboarduser = _dashboarduserManager.update(foundDashboarduser);

			List<DashboardversionreportEntity> dvrList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "published", userId);
			for(DashboardversionreportEntity dvr : dvrList)
			{
				DashboardversionreportEntity updateDashboardreport = dashboardversionMapper.dashboardversionreportEntityToDashboardversionreportEntity(dvr, userId, "running");
				_reportDashboardManager.update(updateDashboardreport);
				_reportAppService.resetReport(userId,dvr.getReportId());

			}

			List<DashboardversionreportEntity> dvrRunningList = _reportDashboardManager.findByDashboardIdAndVersionAndUserId(dashboardId, "running", userId);
			for(DashboardversionreportEntity dvr : dvrRunningList)
			{
				if(!dvrList.stream().filter(o -> o.getReportId().equals(dvr.getReportId())).findFirst().isPresent()) {
					_reportDashboardManager.delete(dvr);
				}

			}

			if(foundDashboarduser!=null && !foundDashboarduser.getEditable()) {
				_dashboardversionManager.delete(publishedversion);
			}

			FindDashboardByIdOutput output = mapper.dashboardEntitiesToFindDashboardByIdOutput(foundDashboard,runningversion,foundDashboarduser);
			output.setSharedWithMe(true);
			return output;
		}

		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Dashboard")
	public List<FindDashboardByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<DashboardEntity> foundDashboard = _dashboardManager.findAll(search(search), pageable);
		List<DashboardEntity> dashboardList = foundDashboard.getContent();
		Iterator<DashboardEntity> dashboardIterator = dashboardList.iterator(); 
		List<FindDashboardByIdOutput> output = new ArrayList<>();

		while (dashboardIterator.hasNext()) {
			DashboardEntity dashboard = dashboardIterator.next();
			DashboardversionEntity dashboardVersion =_dashboardversionManager.findById(new DashboardversionId(dashboard.getUser().getId(),dashboard.getId(), "running"));
			DashboarduserEntity dashboarduser =  _dashboarduserManager.findById(new DashboarduserId(dashboard.getId(), dashboardVersion.getUserId()));

			FindDashboardByIdOutput dashboardOutput  = mapper.dashboardEntitiesToFindDashboardByIdOutput(dashboard, dashboardVersion, dashboarduser); 
			DashboardversionEntity publishedversion = _dashboardversionManager.findById(new DashboardversionId(dashboard.getUser().getId(), dashboard.getId(), "published"));
			if(publishedversion == null)
			{
				dashboardOutput.setIsResetable(false);
			}
			else 
				dashboardOutput.setIsResetable(true);

			output.add(dashboardOutput); 

		}
		return output;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput addNewReportsToNewDashboard(AddNewReportToNewDashboardInput input)
	{
		CreateDashboardInput dashboardInput = mapper.addNewReportToNewDashboardInputTocreatDashboardInput(input);
		dashboardInput.setIsShareable(true);
		CreateDashboardOutput createdDashboard = create(dashboardInput);

		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();

		for(CreateReportInput report : input.getReportDetails())
		{
			report.setIsPublished(true);
			report.setOwnerId(createdDashboard.getOwnerId());
			report.setIsAssignedByDashboard(false);
			CreateReportOutput createdReport = _reportAppService.create(report);
			if(report.getReportWidth() !=null) {
				createdReport.setReportWidth(report.getReportWidth());
			}
			else
				createdReport.setReportWidth("mediumchart");
			reportList.add(createdReport);
			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(createdReport,null);
			output.setReportWidth(report.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		dashboardOuputDto.setIsResetable(false);
		return dashboardOuputDto;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput addNewReportsToExistingDashboard(AddNewReportToExistingDashboardInput input)
	{
		DashboardversionEntity dashboardversion = _dashboardversionManager.findById(new DashboardversionId(input.getOwnerId(), input.getId(),"running"));
		DashboardEntity dashboard = _dashboardManager.findById(input.getId());

		CreateDashboardOutput createdDashboard = mapper.dashboardEntityAndDashboardversionEntityToCreateDashboardOutput(dashboard, dashboardversion);

		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();

		for(CreateReportInput report : input.getReportDetails())
		{
			report.setIsPublished(true);
			report.setOwnerId(createdDashboard.getOwnerId());
			report.setIsAssignedByDashboard(false);
			CreateReportOutput createdReport = _reportAppService.create(report);
			if(report.getReportWidth() !=null) {
				createdReport.setReportWidth(report.getReportWidth());
			}
			else
				createdReport.setReportWidth("mediumchart");

			reportList.add(createdReport);
			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(createdReport,null);
			output.setReportWidth(report.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		//	_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		dashboardOuputDto.setIsResetable(true);

		DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(input.getId(), input.getOwnerId()));

		if(dashboarduser !=null)
		{
			dashboarduser.setIsResetted(false);
			//		dashboard.setIsShareable(false);
			_dashboarduserManager.update(dashboarduser);
		}
		if(dashboard.getUser() !=null && dashboard.getUser().getId() == input.getOwnerId())
		{
			dashboard.setIsPublished(false);
			dashboard.setIsShareable(true);
			_dashboardManager.update(dashboard);
		}

		//		_dashboardManager.update(dashboard);

		return dashboardOuputDto;

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput addExistingReportsToNewDashboard(AddExistingReportToNewDashboardInput input)
	{
		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();

		Boolean sharable = true;
		for(ExistingReportInput report : input.getReportDetails())
		{
			ReportEntity reportEntity = _reportManager.findById(report.getId());
			ReportversionEntity reportversionEntity = _reportversionManager.findById(new ReportversionId(input.getOwnerId(),report.getId(),"published"));
			if(reportversionEntity == null)
			{
				reportversionEntity = _reportversionManager.findById(new ReportversionId(input.getOwnerId(),report.getId(),"running"));
			}

			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(report.getId(), input.getOwnerId()));

			if(reportuser != null) {
				sharable=false;
			}

			CreateReportOutput reportOutput = reportMapper.reportEntityAndReportversionEntityToCreateReportOutput(reportEntity, reportversionEntity);
			if(report.getReportWidth() !=null) {
				reportOutput.setReportWidth(report.getReportWidth());
			}
			else
				reportOutput.setReportWidth("mediumchart");
			reportList.add(reportOutput);

			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(reportOutput, reportuser);
			output.setReportWidth(reportOutput.getReportWidth());
			reportsOutput.add(output);
		}

		CreateDashboardInput dashboardInput = mapper.addExistingReportToNewDashboardInputTocreatDashboardInput(input);
		dashboardInput.setIsShareable(sharable);
		CreateDashboardOutput createdDashboard = create(dashboardInput);

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		dashboardOuputDto.setIsResetable(false);

		return dashboardOuputDto;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindDashboardByIdOutput addExistingReportsToExistingDashboard(AddExistingReportToExistingDashboardInput input)
	{		
		DashboardEntity dashboard = _dashboardManager.findById(input.getId());
		DashboardversionEntity dashboardversion = _dashboardversionManager.findById(new DashboardversionId(input.getOwnerId(), dashboard.getId(),"running"));
		DashboarduserEntity dashboarduser = _dashboarduserManager.findById(new DashboarduserId(input.getId(), input.getOwnerId()));

		CreateDashboardOutput createdDashboard = mapper.dashboardEntityAndDashboardversionEntityToCreateDashboardOutput(dashboard, dashboardversion);

		Boolean sharable = true;

		List<FindReportByIdOutput> reportsOutput =new ArrayList<>();
		List<CreateReportOutput> reportList = new ArrayList<>();
		for(ExistingReportInput report : input.getReportDetails())
		{
			ReportEntity reportEntity = _reportManager.findById(report.getId());
			ReportversionEntity reportversionEntity = _reportversionManager.findById(new ReportversionId(input.getOwnerId(),reportEntity.getId(),"published"));
			if(reportversionEntity == null)
			{
				reportversionEntity = _reportversionManager.findById(new ReportversionId(input.getOwnerId(),reportEntity.getId(),"running"));
			}

			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(report.getId(), input.getOwnerId()));

			if(reportuser !=null ) {
				sharable =false;
				// && dashboarduser !=null
				//	dashboarduser.setOwnerSharingStatus(false);
			}

			CreateReportOutput reportOutput = reportMapper.reportEntityAndReportversionEntityToCreateReportOutput(reportEntity, reportversionEntity);
			if(report.getReportWidth() !=null) {
				reportOutput.setReportWidth(report.getReportWidth());
			}
			else
				reportOutput.setReportWidth("mediumchart");
			reportList.add(reportOutput);

			FindReportByIdOutput output = reportMapper.createReportOutputToFindReportByIdOutput(reportOutput, reportuser);
			output.setReportWidth(reportOutput.getReportWidth());
			reportsOutput.add(output);
		}

		_reportDashboardAppService.addReportsToDashboardRunningversion(createdDashboard, reportList);
		//		_reportDashboardAppService.addReportsToDashboardPublishedversion(createdDashboard, reportList);

		FindDashboardByIdOutput dashboardOuputDto = mapper.dashboardOutputToFindDashboardByIdOutput(createdDashboard);
		dashboardOuputDto.setReportDetails(reportsOutput);
		dashboardOuputDto.setIsResetable(true);

		if(dashboarduser !=null)
		{
			dashboarduser.setIsResetted(false);
			_dashboarduserManager.update(dashboarduser);
		}

		if(dashboard.getUser() !=null && dashboard.getUser().getId() == input.getOwnerId())
		{
			dashboard.setIsPublished(false);
			dashboard.setIsShareable(sharable);
			_dashboardManager.update(dashboard);

			List<DashboarduserEntity> dashboardusersList = _dashboarduserManager.findByDashboardId(dashboard.getId());
			for(DashboarduserEntity du : dashboardusersList)
			{
				du.setOwnerSharingStatus(sharable);
				_dashboarduserManager.update(du);
			}
		}
		//	_dashboardManager.update(dashboard);

		return dashboardOuputDto;
	}


	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QDashboardEntity dashboard= QDashboardEntity.dashboardEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(dashboard, map,search.getJoinColumns());
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

	public BooleanBuilder searchKeyValuePair(QDashboardEntity dashboard, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("isPublished")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(dashboard.isPublished.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(dashboard.isPublished.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("ownerId")) {
				builder.and(dashboard.user.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}


	public Map<String,String> parseReportdashboardJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("dashboardId", keysString);
		return joinColumnMap;
	}


}




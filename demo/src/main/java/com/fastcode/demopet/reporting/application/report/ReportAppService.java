package com.fastcode.demopet.reporting.application.report;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.authorization.userrole.UserroleManager;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboarduserId;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportId;
import com.fastcode.demopet.domain.model.QReportEntity;
import com.fastcode.demopet.domain.model.ReportEntity;
import com.fastcode.demopet.domain.model.ReportroleEntity;
import com.fastcode.demopet.domain.model.ReportroleId;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportuserId;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.fastcode.demopet.domain.model.ReportversionId;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.reporting.application.report.dto.*;
import com.fastcode.demopet.reporting.application.reportrole.IReportroleAppService;
import com.fastcode.demopet.reporting.application.reportrole.dto.CreateReportroleInput;
import com.fastcode.demopet.reporting.application.reportuser.IReportuserAppService;
import com.fastcode.demopet.reporting.application.reportuser.dto.CreateReportuserInput;
import com.fastcode.demopet.reporting.application.reportversion.IReportversionMapper;
import com.fastcode.demopet.reporting.application.reportversion.ReportversionAppService;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.CreateReportversionOutput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionInput;
import com.fastcode.demopet.reporting.application.reportversion.dto.UpdateReportversionOutput;
import com.fastcode.demopet.reporting.domain.dashboarduser.IDashboarduserManager;
import com.fastcode.demopet.reporting.domain.dashboardversionreport.IDashboardversionreportManager;
import com.fastcode.demopet.reporting.domain.report.IReportManager;
import com.fastcode.demopet.reporting.domain.reportrole.IReportroleManager;
import com.fastcode.demopet.reporting.domain.reportuser.IReportuserManager;
import com.fastcode.demopet.reporting.domain.reportversion.IReportversionManager;
import com.querydsl.core.BooleanBuilder;

import net.bytebuddy.description.modifier.Ownership;

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
public class ReportAppService implements IReportAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private IReportManager _reportManager;

	@Autowired
	private IReportuserManager _reportuserManager;

	@Autowired
	private IDashboarduserManager _dashboarduserManager;

	@Autowired
	private IReportversionManager _reportversionManager;

	@Autowired
	private UserManager _userManager;

	@Autowired
	private IDashboardversionreportManager _reportDashboardManager;

	@Autowired
	private ReportversionAppService _reportversionAppservice;

	@Autowired
	private UserroleManager _userroleManager;

	@Autowired
	private IReportuserAppService _reportuserAppservice;

	@Autowired
	private IReportroleAppService _reportroleAppservice;

	@Autowired
	private IReportroleManager _reportroleManager;

	@Autowired
	private ReportMapper mapper;

	@Autowired
	private IReportversionMapper reportversionMapper;

	@Autowired
	private LoggingHelper logHelper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateReportOutput create(CreateReportInput input) {

		ReportEntity report = mapper.createReportInputToReportEntity(input);
		if(input.getOwnerId()!=null) {
			UserEntity foundUser = _userManager.findById(input.getOwnerId());
			if(foundUser!=null) {
				report.setUser(foundUser);
			}
		}

		ReportEntity createdReport = _reportManager.create(report);
		CreateReportversionInput reportversion = mapper.createReportInputToCreateReportversionInput(input);
		reportversion.setReportId(createdReport.getId());

		CreateReportversionOutput reportversionOutput = _reportversionAppservice.create(reportversion);

		return mapper.reportEntityAndCreateReportversionOutputToCreateReportOutput(createdReport, reportversionOutput);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Report", key = "#p0")
	public UpdateReportOutput update(Long  reportId, UpdateReportInput input) {

		//	ReportEntity report = mapper.updateReportInputToReportEntity(input);

		ReportversionId reportversionId = new ReportversionId(input.getUserId(), reportId, "running");

		ReportversionEntity rv = _reportversionManager.findById(reportversionId);
		UpdateReportversionInput reportversion = mapper.updateReportInputToUpdateReportversionInput(input);
		reportversion.setReportId(rv.getReport().getId());
		reportversion.setReportVersion(rv.getReportVersion());
        reportversion.setVersion(rv.getVersion());
        
		UpdateReportversionOutput reportversionOutput =  _reportversionAppservice.update(reportversionId, reportversion);

		ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, input.getUserId()));
		if(reportuser !=null)
		{
			reportuser.setIsResetted(false);
			_reportuserManager.update(reportuser);
		}

		ReportEntity foundReport = _reportManager.findById(reportId);
		if(foundReport.getUser() !=null && foundReport.getUser().getId() == input.getUserId())
		{
			foundReport.setIsPublished(false);
			_reportManager.update(foundReport);
		}


		return mapper.reportEntityAndUpdateReportversionOutputToUpdateReportOutput(foundReport,reportversionOutput);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Report", key = "#p0")
	public void delete(Long reportId, Long userId) {

		ReportEntity existing = _reportManager.findById(reportId) ; 

		_reportversionAppservice.delete(new ReportversionId(userId, reportId, "running"));
		_reportversionAppservice.delete(new ReportversionId(userId, reportId, "published"));

		List<ReportuserEntity> reportUserList = _reportuserManager.findByReportId(existing.getId());
		for(ReportuserEntity reportuser : reportUserList)
		{
			reportuser.setOwnerSharingStatus(false);
			_reportuserManager.update(reportuser);
		}

		existing.setUser(null);
		existing.setIsPublished(true);
		_reportManager.update(existing);

	}

	//	@Transactional(propagation = Propagation.REQUIRED)
	//	@CacheEvict(value="Report", key = "#p0")
	//	public void delete(Long reportId, Long userId) {
	//
	//		ReportEntity existing = _reportManager.findByReportIdAndUserId(reportId, userId);
	//		_reportManager.delete(existing);
	//		
	//	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public FindReportByIdOutput findById(Long reportId) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		if (foundReport == null)  
			return null ; 

		ReportversionEntity reportversion = _reportversionManager.findById(new ReportversionId(foundReport.getUser().getId(), foundReport.getId(), "running"));

		FindReportByIdOutput output=mapper.reportEntityToFindReportByIdOutput(foundReport,reportversion); 
		return output;
	}
	//User
	// ReST API Call - GET /report/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Report", key="#p0")
	public GetUserOutput getUser(Long reportId) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		if (foundReport == null) {
			logHelper.getLogger().error("There does not exist a report with a id=%s", reportId);
			return null;
		}
		UserEntity re = _reportManager.getUser(reportId);

		return mapper.userEntityToGetUserOutput(re, foundReport);
	}

	//User
	// ReST API Call - GET /report/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Report", key="#p0")
	public List<GetUserOutput> getUsersAssociatedWithReport(Long reportId, String search, Pageable pageable) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		Page<UserEntity> foundUser = _reportuserManager.getReportUsersList(reportId,search,pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator();
		List<GetUserOutput> usersList = new ArrayList<>();

		while (userIterator.hasNext()) {
			UserEntity user = userIterator.next();
			GetUserOutput output = mapper.userEntityToGetUserOutput(user, foundReport);
			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, user.getId()));
			if(reportuser != null)
			{
				output.setEditable(reportuser.getEditable());
			}
			usersList.add(output);
		}

		return usersList;
	}

	//User
	// ReST API Call - GET /report/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Report", key="#p0")
	public List<GetUserOutput> getAvailableUsers(Long reportId, String search, Pageable pageable) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		Page<UserEntity> foundUser = _reportuserManager.getAvailableUsersList(reportId,search,pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator();
		List<GetUserOutput> usersList = new ArrayList<>();

		while (userIterator.hasNext()) {
			UserEntity user = userIterator.next();
			GetUserOutput output = mapper.userEntityToGetUserOutput(user, foundReport);
			ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, user.getId()));
			if(reportuser != null)
			{
				output.setEditable(reportuser.getEditable());
			}
			usersList.add(output);
		}

		return usersList;
	}

	//Role
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Report", key="#p0")
	public List<GetRoleOutput> getRolesAssociatedWithReport(Long reportId, String search, Pageable pageable) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		Page<RoleEntity> foundRole = _reportroleManager.getReportRolesList(reportId,search,pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator();
		List<GetRoleOutput> rolesList = new ArrayList<>();

		while (roleIterator.hasNext()) {
			RoleEntity role = roleIterator.next();
			ReportroleEntity reportrole = _reportroleManager.findById(new ReportroleId(reportId, role.getId()));
			GetRoleOutput output = mapper.roleEntityToGetRoleOutput(role, foundReport);
			if(reportrole != null)
			{
				output.setEditable(reportrole.getEditable());
			}
			rolesList.add(output);
		}

		return rolesList;
	}

	//User
	// ReST API Call - GET /report/1/user
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable (value = "Report", key="#p0")
	public List<GetRoleOutput> getAvailableRoles(Long reportId, String search, Pageable pageable) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		Page<RoleEntity> foundRole = _reportroleManager.getAvailableRolesList(reportId,search,pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator();
		List<GetRoleOutput> rolesList = new ArrayList<>();

		while (roleIterator.hasNext()) {
			RoleEntity role = roleIterator.next();
			GetRoleOutput output = mapper.roleEntityToGetRoleOutput(role, foundReport);
			rolesList.add(output);
		}

		return rolesList;

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public FindReportByIdOutput findByReportIdAndUserId(Long reportId, Long userId) {

		ReportEntity foundReport = _reportManager.findByReportIdAndUserId(reportId, userId);
		ReportuserEntity reportuser =  _reportuserManager.findById(new ReportuserId(reportId, userId));

		if (foundReport == null && reportuser == null )  {
			return null;
		}

		ReportversionEntity reportVersion =_reportversionManager.findById(new ReportversionId(userId, reportId, "running"));
		FindReportByIdOutput output  = mapper.reportEntitiesToFindReportByIdOutput(foundReport, reportVersion, reportuser); 
		ReportversionEntity publishedversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "published"));


		if(reportuser != null)
		{
			output.setSharedWithMe(true);
		}

		List<ReportuserEntity> reportuserList = _reportuserManager.findByReportId(reportId);
		if(reportuserList !=null && !reportuserList.isEmpty()) {
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

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public ReportDetailsOutput updateRecipientSharingStatus (Long userId, Long reportId, Boolean status) {

		ReportuserEntity foundReportuser = _reportuserManager.findById(new ReportuserId(reportId, userId));
		if(foundReportuser ==null)
			return null;
		foundReportuser.setRecipientSharingStatus(status);
		foundReportuser = _reportuserManager.update(foundReportuser);

		ReportversionEntity foundReportversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "running"));
		ReportEntity foundReport = _reportManager.findById(reportId);

		return mapper.reportEntitiesToReportDetailsOutput(foundReport,foundReportversion,foundReportuser);

	}

	//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public ReportDetailsOutput publishReport(Long userId, Long reportId) {

		ReportEntity foundReport = _reportManager.findById(reportId);

		List<ReportuserEntity> reportuserList = _reportuserManager.findByReportId(reportId);
		for(ReportuserEntity reportuser: reportuserList)
		{
			reportuser.setIsRefreshed(false);
			_reportuserManager.update(reportuser);
		}

		foundReport.setIsPublished(true);
		foundReport = _reportManager.update(foundReport);
		ReportversionEntity foundReportversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "running"));
		ReportversionEntity foundPublishedversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "published"));
		ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(foundReportversion, userId, "published");
		
		if(foundPublishedversion !=null)
		{
			publishedVersion.setVersion(foundPublishedversion.getVersion());
		}
		else
			publishedVersion.setVersion(0L);
		
		_reportversionManager.update(publishedVersion);

		return mapper.reportEntitiesToReportDetailsOutput(foundReport,foundReportversion,null);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public ReportDetailsOutput changeOwner(Long ownerId, Long reportId, Long newOwnerId) {

		ReportEntity foundReport = _reportManager.findById(reportId);
		UserEntity foundUser = _userManager.findById(newOwnerId);

		ReportuserEntity ru = _reportuserManager.findById(new ReportuserId(reportId, newOwnerId));
		ReportversionEntity reportRunningversion;
		if(ru != null) {

			reportRunningversion = _reportversionManager.findById(new ReportversionId(newOwnerId, reportId, "running"));
			ReportversionEntity reportPublishedversion = _reportversionManager.findById(new ReportversionId(newOwnerId, reportId, "published"));
			if(reportPublishedversion == null) {
				reportPublishedversion = reportversionMapper.reportversionEntityToReportversionEntity(reportRunningversion, newOwnerId, "published");
				reportPublishedversion.setUser(foundUser);
				_reportversionManager.create(reportPublishedversion);
			}

			_reportuserManager.delete(ru);
		}
		else
		{
			ReportversionEntity foundOwnerReportRunningversion = _reportversionManager.findById(new ReportversionId(ownerId, reportId, "running"));
			ReportversionEntity foundOwnerReportPublishedversion = _reportversionManager.findById(new ReportversionId(ownerId, reportId, "published"));
			reportRunningversion = reportversionMapper.reportversionEntityToReportversionEntity(foundOwnerReportRunningversion, foundUser.getId(), "running");
			reportRunningversion.setUser(foundUser);
			_reportversionManager.create(reportRunningversion);

			ReportversionEntity reportPublishedversion = reportversionMapper.reportversionEntityToReportversionEntity(foundOwnerReportPublishedversion, foundUser.getId(), "published");

			reportPublishedversion.setUser(foundUser);
			_reportversionManager.create(reportPublishedversion);			
		}

		_reportversionAppservice.delete(new ReportversionId(ownerId, reportId, "running"));
		_reportversionAppservice.delete(new ReportversionId(ownerId, reportId, "published"));

		List<DashboardversionreportEntity> dvrRunningversionsList = _reportDashboardManager.findByReportIdAndUserIdAndVersion(reportId,ownerId, "running");

		for(DashboardversionreportEntity dvr : dvrRunningversionsList)
		{

			List<DashboardversionreportEntity> sharedDashboardReportList = _reportDashboardManager.findByIdIfUserIdIsNotSame(dvr.getDashboardId(), reportId,ownerId, "running");
			for(DashboardversionreportEntity shared : sharedDashboardReportList)
			{
				DashboarduserEntity du = _dashboarduserManager.findById(new DashboarduserId(shared.getDashboardId(), shared.getUserId()));
				if(du !=null) {
					du.setIsRefreshed(false);
					_dashboarduserManager.update(du);
				}
			}

			DashboardversionreportEntity ownerPublishedDashboardReport = _reportDashboardManager.findById(new DashboardversionreportId(dvr.getDashboardId(), ownerId, "published", reportId));

			_reportDashboardManager.delete(dvr);
			if(ownerPublishedDashboardReport !=null)
				_reportDashboardManager.delete(ownerPublishedDashboardReport);
		}

		foundReport.setUser(foundUser);
		_reportManager.update(foundReport);


		return mapper.reportEntitiesToReportDetailsOutput(foundReport,reportRunningversion,null);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public ReportDetailsOutput refreshReport(Long userId, Long reportId) {

		ReportuserEntity foundReportuser = _reportuserManager.findById(new ReportuserId(reportId, userId));
		ReportEntity foundReport = _reportManager.findById(reportId);

		if(foundReportuser !=null && foundReportuser.getOwnerSharingStatus()) {

			ReportversionEntity ownerPublishedversion = _reportversionManager.findById(new ReportversionId(foundReport.getUser().getId(), reportId, "published"));
			UserEntity foundUser = _userManager.findById(userId);

			ReportversionEntity publishedversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "published"));
			ReportversionEntity updatedVersion;
			if(publishedversion == null ) {
				updatedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedversion, userId, "published"); 

			}
			else
			{
				updatedVersion = reportversionMapper.reportversionEntityToReportversionEntity(publishedversion, userId, "running"); 
			}

			updatedVersion.setUser(foundUser);
			_reportversionManager.update(updatedVersion);
			foundReportuser.setIsRefreshed(true);
			foundReportuser.setIsResetted(false);
			foundReportuser = _reportuserManager.update(foundReportuser);

			ReportversionEntity runningversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "running"));
			ReportDetailsOutput output = mapper.reportEntitiesToReportDetailsOutput(foundReport,runningversion,foundReportuser);
			output.setSharedWithMe(true);
			
			return output;
		}

		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	public ReportDetailsOutput resetReport(Long userId, Long reportId) {

		ReportuserEntity foundReportuser = _reportuserManager.findById(new ReportuserId(reportId, userId));
		ReportEntity foundReport = _reportManager.findById(reportId);


		ReportversionEntity publishedversion = _reportversionManager.findById(new ReportversionId(userId, reportId, "published"));
		if(publishedversion !=null)
		{
			ReportversionEntity runningversion = reportversionMapper.reportversionEntityToReportversionEntity(publishedversion, userId, "running");
			//	runningversion.setVersion("running");
			runningversion=_reportversionManager.update(runningversion);
			if(foundReportuser !=null && !foundReportuser.getEditable()) {
				_reportversionManager.delete(publishedversion);
			}

			foundReportuser.setIsResetted(true);
			foundReportuser = _reportuserManager.update(foundReportuser);

			ReportDetailsOutput output = mapper.reportEntitiesToReportDetailsOutput(foundReport,runningversion,foundReportuser);
			output.setSharedWithMe(true);
			
			return output;
		}

		return null;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ReportDetailsOutput editReportAccess(Long reportId, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {

		ReportEntity report = _reportManager.findById(reportId);
		ReportversionEntity ownerPublishedVersion = _reportversionManager.findById(new ReportversionId(report.getUser().getId(), report.getId(), "published"));
		if(ownerPublishedVersion==null)
		{
			return null;
		}

		List<Long> usersWithSharedReportsByRole = new ArrayList<>();
		for(ShareReportInput roleInput : rolesList)
		{
			ReportroleEntity reportRole= _reportroleManager.findById(new ReportroleId(reportId, roleInput.getId()));
			if(reportRole !=null && roleInput.getEditable() != null)
			{
				reportRole.setEditable(roleInput.getEditable());
				reportRole.setOwnerSharingStatus(true);
				_reportroleManager.update(reportRole);
			}
			else if(reportRole !=null && roleInput.getEditable() == null)
			{
				reportRole.setOwnerSharingStatus(false);
				_reportroleManager.update(reportRole);
			}

			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
			for(UserroleEntity userrole : userroleList)
			{
				if(!userrole.getUserId().equals(report.getUser().getId())) {
					usersWithSharedReportsByRole.add(userrole.getUserId());
					ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userrole.getUserId()));

					if(reportuser !=null && reportuser.getIsAssignedByRole())
					{
						if(reportuser !=null && roleInput.getEditable() !=null) {
							shareReportWithUser(reportuser,ownerPublishedVersion, roleInput.getEditable());
							reportuser.setEditable(roleInput.getEditable());
							reportuser.setIsAssignedByRole(true);
							 _reportuserManager.update(reportuser);

						}
						else if (roleInput.getEditable() == null && reportuser !=null) {

							reportuser.setOwnerSharingStatus(false);
							 _reportuserManager.update(reportuser);

						}
					}
				}
			}


		}

		for(ShareReportInput userInput : usersList)
		{
			if(!userInput.getId().equals(report.getUser().getId()) && !usersWithSharedReportsByRole.contains(userInput.getId())) {
				ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userInput.getId()));
				if(reportuser !=null && !reportuser.getIsAssignedByRole())
				{
					if(reportuser !=null && userInput.getEditable() !=null) {
						shareReportWithUser(reportuser,ownerPublishedVersion, userInput.getEditable());
						reportuser.setEditable(userInput.getEditable());
						reportuser.setIsAssignedByRole(false);
						 _reportuserManager.update(reportuser);

					}
					else if (userInput.getEditable() == null && reportuser !=null) {

						reportuser.setOwnerSharingStatus(false);
						 _reportuserManager.update(reportuser);
					}
				}
			}

		}

		ReportDetailsOutput reportDetails = mapper.reportEntitiesToReportDetailsOutput(report, ownerPublishedVersion, null);
		return reportDetails;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public ReportDetailsOutput shareReport(Long reportId, Boolean isAssignedByDashboard, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {
		ReportEntity report = _reportManager.findById(reportId);
		ReportversionEntity ownerPublishedVersion = _reportversionManager.findById(new ReportversionId(report.getUser().getId(), report.getId(), "published"));

		if(ownerPublishedVersion==null)
		{
			return null;
		}

		List<Long> usersWithSharedReportsByRole = new ArrayList<>();
		for(ShareReportInput roleInput : rolesList)
		{
			ReportroleEntity reportrole = _reportroleManager.findById(new ReportroleId(reportId, roleInput.getId()));
			if(reportrole == null) {
			CreateReportroleInput reportRoleInput = new CreateReportroleInput();
			reportRoleInput.setRoleId(roleInput.getId());
			reportRoleInput.setReportId(reportId);
			reportRoleInput.setEditable(roleInput.getEditable());
			reportRoleInput.setOwnerSharingStatus(true);
			_reportroleAppservice.create(reportRoleInput);
			}
			else if (reportrole !=null && !reportrole.getOwnerSharingStatus())
			{
				reportrole.setOwnerSharingStatus(true);
				reportrole.setEditable(roleInput.getEditable());
				_reportroleManager.update(reportrole);
			}

			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
			for(UserroleEntity userrole : userroleList)
			{
				usersWithSharedReportsByRole.add(userrole.getUserId());
				ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userrole.getUserId()));
				
				if(reportuser !=null ) {
					if(!report.getUser().getId().equals( reportuser.getUser().getId())) {
					
					shareReportWithUser(reportuser,ownerPublishedVersion, roleInput.getEditable());
					reportuser.setEditable(roleInput.getEditable());
					reportuser.setIsAssignedByRole(true);
					reportuser = _reportuserManager.update(reportuser);
					}

				}
				else {
					createReportuserAndReportVersion(ownerPublishedVersion,userrole.getUserId(), roleInput.getEditable(),true, isAssignedByDashboard);
				}
			}

		}

		for(ShareReportInput userInput : usersList)
		{
			if(!usersWithSharedReportsByRole.contains(userInput.getId())) {

				ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userInput.getId()));

				if(reportuser !=null ) {
					if(!report.getUser().getId().equals(userInput.getId())) {
					shareReportWithUser(reportuser,ownerPublishedVersion, userInput.getEditable());
					reportuser.setEditable(userInput.getEditable());
					reportuser.setIsAssignedByRole(false);
					reportuser = _reportuserManager.update(reportuser);
					}
				}

				else {
					createReportuserAndReportVersion(ownerPublishedVersion,userInput.getId(),userInput.getEditable(),false,isAssignedByDashboard);
				}
			}
		}

		ReportDetailsOutput reportDetails = mapper.reportEntitiesToReportDetailsOutput(report, ownerPublishedVersion, null);
		reportDetails.setSharedWithOthers(true);
		return reportDetails;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void createReportuserAndReportVersion(ReportversionEntity ownerReportversion, Long userId, Boolean editable, Boolean isAssigByRole, Boolean isAssignedByDashboard)
	{
		CreateReportuserInput createReportuserInput = new CreateReportuserInput();
		createReportuserInput.setReportId(ownerReportversion.getReportId());
		createReportuserInput.setUserId(userId);
		createReportuserInput.setEditable(editable);
		createReportuserInput.setIsAssignedByRole(isAssigByRole);
		createReportuserInput.setIsResetted(true);
		createReportuserInput.setIsRefreshed(true);
		createReportuserInput.setOwnerSharingStatus(true);
		createReportuserInput.setRecipientSharingStatus(true);
		_reportuserAppservice.create(createReportuserInput);

		UserEntity user = _userManager.findById(userId);
		if(editable) {
			ReportversionEntity publishedreportversion = reportversionMapper.reportversionEntityToReportversionEntity(ownerReportversion, user.getId(), "published"); 
			publishedreportversion.setUser(user);
			publishedreportversion.setIsAssignedByDashboard(isAssignedByDashboard);
			_reportversionManager.create(publishedreportversion);
			ReportversionEntity runningreportversion = reportversionMapper.reportversionEntityToReportversionEntity(ownerReportversion, user.getId(), "running"); 
			runningreportversion.setUser(user);
			runningreportversion.setIsAssignedByDashboard(isAssignedByDashboard);
			_reportversionManager.create(runningreportversion);
		}
		else {
			ReportversionEntity runningreportversion = reportversionMapper.reportversionEntityToReportversionEntity(ownerReportversion, user.getId(), "running"); 
			runningreportversion.setUser(user);
			runningreportversion.setIsAssignedByDashboard(isAssignedByDashboard);
			_reportversionManager.create(runningreportversion);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void shareReportWithUser(ReportuserEntity reportuser, ReportversionEntity ownerPublishedVersion, Boolean editable)
	{
		UserEntity user = _userManager.findById(reportuser.getUserId());

		ReportversionEntity reportPublishedVersion = _reportversionManager.findById(new ReportversionId(user.getId(),reportuser.getReportId(),"published"));
        ReportversionEntity reportRunningVersion = _reportversionManager.findById(new ReportversionId(user.getId(),reportuser.getReportId(),"running"));
		
        if(reportuser.getEditable() && !editable) {
			
            if(reportuser.getOwnerSharingStatus()) {
			if(reportuser.getIsResetted()) {
				if (reportPublishedVersion != null) {
					_reportversionManager.delete(reportPublishedVersion);	
				}

				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "running"); 
				publishedVersion.setUser(user);
				_reportversionManager.update(publishedVersion);
			}
			else if(!reportuser.getIsResetted()) {
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_reportversionManager.update(publishedVersion);
			}
            }
            else
            {
            	if(reportuser.getIsResetted()) {
    				if (reportPublishedVersion != null) {
    					_reportversionManager.delete(reportPublishedVersion);	
    				}
            	}
            }

		} else if(!reportuser.getEditable() && !editable) {
			if(reportuser.getOwnerSharingStatus()) {
			if(reportPublishedVersion !=null && !reportuser.getIsResetted()) {
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_reportversionManager.update(publishedVersion);
			}
			else if(reportuser.getIsResetted()) {
				if (reportPublishedVersion != null) {
					_reportversionManager.delete(reportPublishedVersion);	
				}
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "running"); 

				publishedVersion.setUser(user);
				_reportversionManager.update(publishedVersion);
			}
			}


		} else if(reportuser.getEditable() && editable) {
			if(reportuser.getOwnerSharingStatus()) {
			ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "published"); 

			publishedVersion.setUser(user);
			_reportversionManager.update(publishedVersion);
			}

		} else if(!reportuser.getEditable() && editable) {

			if(reportuser.getOwnerSharingStatus()) {
			if(reportPublishedVersion !=null && !reportuser.getIsResetted()) {
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "published"); 

				publishedVersion.setUser(user);
				_reportversionManager.update(publishedVersion);
			}
			else if(reportuser.getIsResetted()) {
				if (reportPublishedVersion != null) {
					_reportversionManager.delete(reportPublishedVersion);	
				}
				
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(ownerPublishedVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_reportversionManager.create(publishedVersion);
			}
			}
			else
			{
				ReportversionEntity publishedVersion = reportversionMapper.reportversionEntityToReportversionEntity(reportRunningVersion, user.getId(), "published"); 
				publishedVersion.setUser(user);
				_reportversionManager.create(publishedVersion);
			}

		}
        
        if(!reportuser.getOwnerSharingStatus()) {
        	reportuser.setOwnerSharingStatus(true);
        	reportuser.setIsRefreshed(false);
        	_reportuserManager.update(reportuser);	
        }
            
	
	}

	//	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	//	@Cacheable(value = "Report", key = "#p0")
	//	public ReportDetailsOutput unshareReport(Long reportId, List<ShareReportInput> usersList, List<ShareReportInput> rolesList) {
	//		ReportEntity report = _reportManager.findById(reportId);
	//		ReportversionEntity ownerPublishedVersion = _reportversionManager.findById(new ReportversionId(report.getUser().getId(), reportId, "published"));
	//		if(ownerPublishedVersion==null)
	//		{
	//			return null;
	//		}
	//		List<Long> usersWithSharedReportsByRole = new ArrayList<>();
	//		for(ShareReportInput roleInput : rolesList)
	//		{	
	//			List<UserroleEntity> userroleList = _userroleManager.findByRoleId(roleInput.getId());
	//			for(UserroleEntity userrole : userroleList)
	//			{
	//				usersWithSharedReportsByRole.add(userrole.getUserId());
	//				ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userrole.getUserId()));
	//
	//				if(reportuser !=null ) {
	//					reportuser.setOwnerSharingStatus(false);
	//					reportuser = _reportuserManager.update(reportuser);
	//				}
	//			}
	//
	//		}
	//
	//		for(ShareReportInput userInput : usersList)
	//		{
	//			if(!usersWithSharedReportsByRole.contains(userInput.getId())) {
	//
	//				ReportuserEntity reportuser = _reportuserManager.findById(new ReportuserId(reportId, userInput.getId()));
	//
	//				if(reportuser != null ) {
	//					reportuser.setOwnerSharingStatus(false);
	//					reportuser = _reportuserManager.update(reportuser);
	//				}
	//			}
	//		}
	//
	//		ReportDetailsOutput reportDetails = mapper.reportEntitiesToReportDetailsOutput(report, ownerPublishedVersion, null);
	//		reportDetails.setSharedWithOthers(false);
	//		return reportDetails;
	//	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<ReportDetailsOutput> getReports(Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<ReportDetailsOutput> foundReport = _reportManager.getReports(userId, search, pageable) ;
		List<ReportDetailsOutput> reportList = foundReport.getContent();

		return reportList ;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<ReportDetailsOutput> getSharedReports(Long userId,String search, Pageable pageable) throws Exception
	{ 
		Page<ReportDetailsOutput> foundReport = _reportManager.getSharedReports(userId, search, pageable) ;
		List<ReportDetailsOutput> reportList = foundReport.getContent();

		return reportList ;
	}


	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Report")
	public List<FindReportByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<ReportEntity> foundReport = _reportManager.findAll(search(search), pageable);
		List<ReportEntity> reportList = foundReport.getContent();
		Iterator<ReportEntity> reportIterator = reportList.iterator(); 
		List<FindReportByIdOutput> output = new ArrayList<>();

		while (reportIterator.hasNext()) {
			ReportEntity report = reportIterator.next();
			ReportversionEntity reportVersion =_reportversionManager.findById(new ReportversionId(report.getUser().getId(),report.getId(), "running"));
			ReportuserEntity reportuser =  _reportuserManager.findById(new ReportuserId(report.getId(), reportVersion.getUserId()));

			FindReportByIdOutput reportOutput  = mapper.reportEntitiesToFindReportByIdOutput(report, reportVersion, reportuser); 
			ReportversionEntity publishedversion = _reportversionManager.findById(new ReportversionId(report.getUser().getId(), report.getId(), "published"));
			if(publishedversion == null)
			{
				reportOutput.setIsResetable(false);
			}
			else 
				reportOutput.setIsResetable(true);

			output.add(reportOutput); 

		}
		return output;
	}

	public void getAllReportsbyUserId(Long userId)
	{

	}

	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QReportEntity report= QReportEntity.reportEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(report, map,search.getJoinColumns());
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

	public BooleanBuilder searchKeyValuePair(QReportEntity report, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("isPublished")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(report.isPublished.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(report.isPublished.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("ownerId")) {
				builder.and(report.user.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}


	public Map<String,String> parseReportdashboardJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("reportId", keysString);
		return joinColumnMap;
	}


}



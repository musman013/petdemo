package com.fastcode.demopet.application.authorization.user;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.security.SecurityUtils;
import com.fastcode.demopet.application.authorization.user.dto.*;
import com.fastcode.demopet.application.processmanagement.ActIdUserMapper;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.owners.IOwnersManager;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;
import com.fastcode.demopet.domain.vets.IVetsManager;
import com.fastcode.demopet.reporting.domain.dashboarduser.DashboarduserManager;
import com.fastcode.demopet.reporting.domain.dashboardversion.IDashboardversionManager;
import com.fastcode.demopet.reporting.domain.dashboardversionreport.IDashboardversionreportManager;
import com.fastcode.demopet.reporting.domain.reportuser.ReportuserManager;
import com.fastcode.demopet.reporting.domain.reportversion.IReportversionManager;
import com.fastcode.demopet.domain.model.DashboarduserEntity;
import com.fastcode.demopet.domain.model.DashboardversionEntity;
import com.fastcode.demopet.domain.model.DashboardversionreportEntity;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.QUserEntity;
import com.fastcode.demopet.domain.model.ReportuserEntity;
import com.fastcode.demopet.domain.model.ReportversionEntity;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import java.util.*;

@Service
@Validated
public class UserAppService implements IUserAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	public static final long PASSWORD_TOKEN_EXPIRATION_TIME = 3_600_000; // 1 hour
	
	@Autowired
	private IUserManager _userManager;
	
	@Autowired
	private IOwnersManager _ownerManager;
	
	@Autowired
	private IVetsManager _vetManager;
	
	@Autowired
	private DashboarduserManager _dashboarduserManager;
	
	@Autowired
	private ReportuserManager _reportuserManager;
	
	@Autowired
	private IDashboardversionManager _dashboardversionManager;
	
	@Autowired
	private IReportversionManager _reportversionManager;
	
	@Autowired
	private IDashboardversionreportManager _reportDashboardManager;

	@Autowired
	private IUserMapper mapper;
	
	@Autowired
 	private ActIdUserMapper actIdUserMapper;
 	
 	@Autowired
 	private FlowableIdentityService idmIdentityService;
 	

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserOutput create(CreateUserInput input) {

		UserEntity user = mapper.createUserInputToUserEntity(input);		
		UserEntity createdUser = _userManager.create(user);
		//Map and create flowable user
 		ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(createdUser);
 		idmIdentityService.createUser(createdUser, actIdUser);
 		
		return mapper.userEntityToCreateUserOutput(createdUser);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateUserOutput update(Long userId, UpdateUserInput input) {

		UserEntity user = mapper.updateUserInputToUserEntity(input);
		UserEntity updatedUser = _userManager.update(user);
		ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(updatedUser);
 		idmIdentityService.updateUser(updatedUser, actIdUser);
		return mapper.userEntityToUpdateUserOutput(updatedUser);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long userId) {

		UserEntity existing = _userManager.findById(userId) ;
		OwnersEntity owner = _ownerManager.findById(userId);
		if(owner !=  null )
		{
			_ownerManager.delete(owner);
		}
		
		VetsEntity vet = _vetManager.findById(userId);
		if(vet !=null)
		{
			_vetManager.delete(vet);
		}
		
		List<DashboardversionreportEntity> dvrList = _reportDashboardManager.findByUserId(userId);
		for(DashboardversionreportEntity dvr : dvrList) {
			_reportDashboardManager.delete(dvr);
		}
		
	    List<DashboarduserEntity> duList = _dashboarduserManager.findByUserId(userId);
	    for(DashboarduserEntity du : duList )
	    {
	    	_dashboarduserManager.delete(du);
	    }
	    
	    List<ReportuserEntity> ruList = _reportuserManager.findByUserId(userId);
	    for(ReportuserEntity du : ruList )
	    {
	    	_reportuserManager.delete(du);
	    }
	    
	   List<DashboardversionEntity> dvList= _dashboardversionManager.findByUserId(userId);
	   for(DashboardversionEntity du : dvList )
	    {
	    	_dashboardversionManager.delete(du);
	    }
	   
	   List<ReportversionEntity> rvList = _reportversionManager.findByUserId(userId);
	   for(ReportversionEntity rv : rvList)
	   {
		   _reportversionManager.delete(rv);
	   }
	    
		_userManager.delete(existing);
		idmIdentityService.deleteUser(existing.getUserName());
	
	}
	@Transactional(readOnly = true)
	public UserEntity getUser() {
	
		return _userManager.findByUserName(SecurityUtils.getCurrentUserLogin().orElse(null));
	}
	
	public Boolean checkIsAdmin(UserEntity user)
	{
		Set<UserroleEntity> ure = user.getUserroleSet();
		Iterator<UserroleEntity> iterator = ure.iterator();
		while(iterator.hasNext())
		{
			UserroleEntity ur = (UserroleEntity) iterator.next();
			if(ur.getRole().getName().equals("ROLE_Admin"))
				return true;
		}

		return false;
	}
	
	public UserProfile getProfile(FindUserByIdOutput user)
	{
		return mapper.findUserByIdOutputToUserProfile(user);
	}
	
	public UserProfile updateUserProfile(FindUserWithAllFieldsByIdOutput user, UserProfile userProfile)
	{
		UpdateUserInput userInput = mapper.findUserWithAllFieldsByIdOutputAndUserProfileToUpdateUserInput(user, userProfile);
		UpdateUserOutput output = update(user.getId(),userInput);
		
		return mapper.updateUserOutputToUserProfile(output);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserByNameOutput findByEmailAddress(String emailAddress) {

		UserEntity foundUser = _userManager.findByEmailAddress(emailAddress);
		if (foundUser == null) {
			return null;
		}
	
		return  mapper.userEntityToFindUserByNameOutput(foundUser);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public UserEntity savePasswordResetCode(String email)
	{
		UserEntity foundUser = _userManager.findByEmailAddress(email);
		if (foundUser == null) {
			return null;
		}
		
		foundUser.setPasswordResetCode(UUID.randomUUID().toString());
		foundUser.setPasswordTokenExpiration(new Date(System.currentTimeMillis() + PASSWORD_TOKEN_EXPIRATION_TIME));
		foundUser = _userManager.update(foundUser);
		
		return foundUser;
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public UserEntity findByPasswordResetCode(String passwordResetCode) {

		UserEntity foundUser = _userManager.findByPasswordResetCode(passwordResetCode);
		if (foundUser == null) {
			return null;
		}
	
		return  foundUser;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserByIdOutput findById(Long userId) {

		UserEntity foundUser = _userManager.findById(userId);
		if (foundUser == null)  
			return null ; 
 	   
 	   FindUserByIdOutput output=mapper.userEntityToFindUserByIdOutput(foundUser); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserByNameOutput findByUserName(String userName) {

		UserEntity foundUser = _userManager.findByUserName(userName);
		if (foundUser == null) {
			return null;
		}
		return  mapper.userEntityToFindUserByNameOutput(foundUser);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserWithAllFieldsByIdOutput findWithAllFieldsById(Long userId) {

		UserEntity foundUser = _userManager.findById(userId);
		if (foundUser == null)  
			return null ; 
 	   
 	    FindUserWithAllFieldsByIdOutput output=mapper.userEntityToFindUserWithAllFieldsByIdOutput(foundUser); 
		return output;
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindUserByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserEntity> foundUser = _userManager.findAll(search(search), pageable);
		List<UserEntity> userList = foundUser.getContent();
		Iterator<UserEntity> userIterator = userList.iterator(); 
		List<FindUserByIdOutput> output = new ArrayList<>();

		while (userIterator.hasNext()) {
			output.add(mapper.userEntityToFindUserByIdOutput(userIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserEntity user= QUserEntity.userEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(user, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
		 list.get(i).replace("%20","").trim().equals("roleId") ||
		
		 list.get(i).replace("%20","").trim().equals("accessFailedCount") ||
		 list.get(i).replace("%20","").trim().equals("authenticationSource") ||
		 list.get(i).replace("%20","").trim().equals("emailAddress") ||
		 list.get(i).replace("%20","").trim().equals("emailConfirmationCode") ||
		 list.get(i).replace("%20","").trim().equals("firstName") ||
		 list.get(i).replace("%20","").trim().equals("id") ||
		 list.get(i).replace("%20","").trim().equals("isEmailConfirmed") ||
		 list.get(i).replace("%20","").trim().equals("isLockoutEnabled") ||
		 list.get(i).replace("%20","").trim().equals("isPhoneNumberConfirmed") ||
		 list.get(i).replace("%20","").trim().equals("lastLoginTime") ||
		 list.get(i).replace("%20","").trim().equals("lastName") ||
		 list.get(i).replace("%20","").trim().equals("lockoutEndDateUtc") ||
		 list.get(i).replace("%20","").trim().equals("isActive") ||
		 list.get(i).replace("%20","").trim().equals("password") ||
		 list.get(i).replace("%20","").trim().equals("passwordResetCode") ||
		 list.get(i).replace("%20","").trim().equals("phoneNumber") ||
		 list.get(i).replace("%20","").trim().equals("profilePictureId") ||
		 list.get(i).replace("%20","").trim().equals("userrole") ||
		 list.get(i).replace("%20","").trim().equals("isTwoFactorEnabled") ||
		 list.get(i).replace("%20","").trim().equals("userName") ||
		 list.get(i).replace("%20","").trim().equals("userpermission")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserEntity user, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("accessFailedCount")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.accessFailedCount.eq(Integer.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.accessFailedCount.ne(Integer.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.accessFailedCount.between(Integer.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(user.accessFailedCount.goe(Integer.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.accessFailedCount.loe(Integer.valueOf(details.getValue().getEndingValue())));
				}
			}
            if(details.getKey().replace("%20","").trim().equals("authenticationSource")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.authenticationSource.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.authenticationSource.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.authenticationSource.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("emailAddress")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.emailAddress.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.emailAddress.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.emailAddress.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("emailConfirmationCode")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.emailConfirmationCode.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.emailConfirmationCode.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.emailConfirmationCode.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("firstName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.firstName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.firstName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.firstName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("isEmailConfirmed")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isEmailConfirmed.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isEmailConfirmed.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("isLockoutEnabled")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isLockoutEnabled.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isLockoutEnabled.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("isPhoneNumberConfirmed")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.isPhoneNumberConfirmed.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.isPhoneNumberConfirmed.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.isPhoneNumberConfirmed.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("lastLoginTime")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lastLoginTime.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lastLoginTime.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(user.lastLoginTime.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(user.lastLoginTime.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(user.lastLoginTime.goe(startDate));  
                 }
                   
			}
            if(details.getKey().replace("%20","").trim().equals("lastName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.lastName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.lastName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.lastName.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("lockoutEndDateUtc")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lockoutEndDateUtc.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(user.lockoutEndDateUtc.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(user.lockoutEndDateUtc.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(user.lockoutEndDateUtc.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(user.lockoutEndDateUtc.goe(startDate));  
                 }
                   
			}
			if(details.getKey().replace("%20","").trim().equals("isActive")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isActive.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isActive.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("password")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.password.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.password.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.password.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("passwordResetCode")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.passwordResetCode.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.passwordResetCode.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.passwordResetCode.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("phoneNumber")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.phoneNumber.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.phoneNumber.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.phoneNumber.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("profilePictureId")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.profilePictureId.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(user.profilePictureId.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.profilePictureId.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(user.profilePictureId.goe(Long.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(user.profilePictureId.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("isTwoFactorEnabled")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isTwoFactorEnabled.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(user.isTwoFactorEnabled.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
            if(details.getKey().replace("%20","").trim().equals("userName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(user.userName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(user.userName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(user.userName.ne(details.getValue().getSearchValue()));
			}
		}
	
		return builder;
	}
	
	public Map<String,String> parseUserpermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", keysString);
		return joinColumnMap;
	}
	
	public Map<String,String> parseUserroleJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("userId", keysString);
		return joinColumnMap;
		
	}

}


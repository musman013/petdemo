package com.fastcode.demopet.application.authorization.userpermission;

import com.fastcode.demopet.application.authorization.userpermission.dto.*;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.domain.authorization.userpermission.IUserpermissionManager;
import com.fastcode.demopet.domain.model.QUserpermissionEntity;
import com.fastcode.demopet.domain.model.UserpermissionEntity;
import com.fastcode.demopet.domain.model.UserpermissionId;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.authorization.permission.IPermissionManager;
import com.fastcode.demopet.domain.model.PermissionEntity;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class UserpermissionAppService implements IUserpermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
 	private FlowableIdentityService idmIdentityService;
	
	@Autowired
	private IUserpermissionManager _userpermissionManager;
  
    @Autowired
	private IUserManager _userManager;
    
    @Autowired
	private IPermissionManager _permissionManager;
    
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private IUserpermissionMapper mapper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserpermissionOutput create(CreateUserpermissionInput input) {

		UserpermissionEntity userpermission = mapper.createUserpermissionInputToUserpermissionEntity(input);
	  	
    	if(input.getUserId()!=null || input.getPermissionId()!=null)
		{
		UserEntity foundUser = _userManager.findById(input.getUserId());
		PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
		
		if(foundUser!=null || foundPermission!=null)
		{			
				if(!checkIfPermissionAlreadyAssigned(foundUser, foundPermission))
				{
					foundUser.addUserpermission(userpermission);
					foundPermission.addUserpermission(userpermission);
					
					idmIdentityService.addUserPrivilegeMapping(foundUser.getUserName(), foundPermission.getName());
					
					CreateUserpermissionOutput output = mapper.userAndPermissionEntityToCreateUserpermissionOutput(foundUser, foundPermission);
					output.setRevoked(input.getRevoked());
					return output;
				}		
		}
		}
		
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateUserpermissionOutput update(UserpermissionId userpermissionId , UpdateUserpermissionInput input) {

		UserpermissionEntity userpermission = mapper.updateUserpermissionInputToUserpermissionEntity(input);
	  	
		if(input.getUserId()!=null || input.getPermissionId()!=null)
		{
			UserEntity foundUser = _userManager.findById(input.getUserId());
			PermissionEntity foundPermission = _permissionManager.findById(input.getPermissionId());
		
			if(foundUser!=null || foundPermission!=null)
			{			
				if(checkIfPermissionAlreadyAssigned(foundUser, foundPermission))
				{
					userpermission.setPermission(foundPermission);
					userpermission.setUser(foundUser);
					userpermission.setRevoked(input.getRevoked());
					
					UserpermissionEntity updatedUserpermission = _userpermissionManager.update(userpermission);
					idmIdentityService.updateUserPrivilegeMapping(updatedUserpermission.getUser().getUserName(), updatedUserpermission.getPermission().getName());
					
					return mapper.userpermissionEntityToUpdateUserpermissionOutput(updatedUserpermission);
				}
			}
		}
		
		return null;
	}
	
	public boolean checkIfPermissionAlreadyAssigned(UserEntity foundUser,PermissionEntity foundPermission)
	{
		
		Set<UserpermissionEntity> userPermission = foundUser.getUserpermissionSet();
		 
		Iterator pIterator = userPermission.iterator();
			while (pIterator.hasNext()) { 
				UserpermissionEntity pe = (UserpermissionEntity) pIterator.next();
				if (pe.getPermission() == foundPermission ) {
					return true;
				}
			}
			
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserpermissionId userpermissionId) {

		UserpermissionEntity existing = _userpermissionManager.findById(userpermissionId) ; 
		UserEntity user = _userManager.findById(userpermissionId.getUserId());
		PermissionEntity permission = _permissionManager.findById(userpermissionId.getPermissionId());
		
		user.removeUserpermission(existing);
		permission.removeUserpermission(existing);
	
		idmIdentityService.deleteUserPrivilegeMapping(user.getUserName(), permission.getName());
		_userpermissionManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserpermissionByIdOutput findById(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null)  
			return null ; 
 	   
 	   FindUserpermissionByIdOutput output=mapper.userpermissionEntityToFindUserpermissionByIdOutput(foundUserpermission); 
		return output;
	}
    //User
	// ReST API Call - GET /userpermission/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetUserOutput getUser(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null) {
			logHelper.getLogger().error("There does not exist a userpermission wth a id=%s", userpermissionId);
			return null;
		}
		UserEntity re = _userpermissionManager.getUser(userpermissionId);
		return mapper.userEntityToGetUserOutput(re, foundUserpermission);
	}
    
    //Permission
	// ReST API Call - GET /userpermission/1/permission
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetPermissionOutput getPermission(UserpermissionId userpermissionId ) {

		UserpermissionEntity foundUserpermission = _userpermissionManager.findById(userpermissionId);
		if (foundUserpermission == null) {
			logHelper.getLogger().error("There does not exist a userpermission wth a id=%s", userpermissionId);
			return null;
		}
		
		PermissionEntity re = _userpermissionManager.getPermission(userpermissionId);
		return mapper.permissionEntityToGetPermissionOutput(re, foundUserpermission);
	}
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindUserpermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserpermissionEntity> foundUserpermission = _userpermissionManager.findAll(search(search), pageable);
		List<UserpermissionEntity> userpermissionList = foundUserpermission.getContent();
		Iterator<UserpermissionEntity> userpermissionIterator = userpermissionList.iterator(); 
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();

		while (userpermissionIterator.hasNext()) {
			output.add(mapper.userpermissionEntityToFindUserpermissionByIdOutput(userpermissionIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserpermissionEntity userpermission= QUserpermissionEntity.userpermissionEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(userpermission, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
    	list.get(i).replace("%20","").trim().equals("userId")||
		 list.get(i).replace("%20","").trim().equals("permission") ||
		 list.get(i).replace("%20","").trim().equals("permissionId") ||
		 list.get(i).replace("%20","").trim().equals("userName")||
	     list.get(i).replace("%20","").trim().equals("name")||
		 list.get(i).replace("%20","").trim().equals("user")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserpermissionEntity userpermission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(userpermission.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        if(joinCol != null && joinCol.getKey().equals("userName")) {
		    builder.and(userpermission.user.userName.eq(joinCol.getValue()));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("permissionId")) {
		    builder.and(userpermission.permission.id.eq(Long.parseLong(joinCol.getValue())));
		}
        if(joinCol != null && joinCol.getKey().equals("name")) {
		    builder.and(userpermission.permission.name.eq(joinCol.getValue()));
		}
        }
		return builder;
	}
	
	public UserpermissionId parseUserpermissionKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		UserpermissionId userpermissionId = new UserpermissionId();
		
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
		
		userpermissionId.setPermissionId(Long.valueOf(keyMap.get("permissionId")));
        userpermissionId.setUserId(Long.valueOf(keyMap.get("userId")));
		return userpermissionId;
	}	
	
	
}



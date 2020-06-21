package com.fastcode.demopet.application.authorization.userrole;

import com.fastcode.demopet.application.authorization.userrole.dto.*;
import com.fastcode.demopet.domain.authorization.userrole.IUserroleManager;
import com.fastcode.demopet.domain.model.QUserroleEntity;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.domain.model.UserroleId;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.authorization.role.RoleManager;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
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
public class UserroleAppService implements IUserroleAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IUserroleManager _userroleManager;
	
    @Autowired
	private UserManager _userManager;
    
    @Autowired
	private RoleManager _roleManager;
    
	@Autowired
	private IUserroleMapper mapper;

	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserroleOutput create(CreateUserroleInput input) {

		UserroleEntity userrole = mapper.createUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.findById(input.getUserId());
	        RoleEntity foundRole = _roleManager.findById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(!checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
				    foundUser.addUserrole(userrole);
					foundRole.addUserrole(userrole);
					
					return mapper.userAndRoleEntityToCreateUserroleOutput(foundUser, foundRole);
				}
		     }
		}
		
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateUserroleOutput update(UserroleId userroleId , UpdateUserroleInput input) {

		UserroleEntity userrole = mapper.updateUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.findById(input.getUserId());
	        RoleEntity foundRole = _roleManager.findById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
					userrole.setRole(foundRole);
					userrole.setUser(foundUser);
					
					UserroleEntity updatedUserrole = _userroleManager.update(userrole);
					return mapper.userroleEntityToUpdateUserroleOutput(updatedUserrole);
				}
		     }
		}
		
		return null;
    }
	
	public boolean checkIfRoleAlreadyAssigned(UserEntity foundUser,RoleEntity foundRole)
	{
		Set<UserroleEntity> userRole = foundUser.getUserroleSet();
		 
		Iterator rIterator = userRole.iterator();
			while (rIterator.hasNext()) { 
				UserroleEntity ur = (UserroleEntity) rIterator.next();
				if (ur.getRole() == foundRole) {
					return true;
				}
			}
			
		return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UserroleId userroleId) {

		UserroleEntity existing = _userroleManager.findById(userroleId) ; 
		UserEntity user = _userManager.findById(userroleId.getUserId());
		RoleEntity role = _roleManager.findById(userroleId.getRoleId());
		
		user.removeUserrole(existing);
		role.removeUserrole(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindUserroleByIdOutput findById(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null)  
			return null ; 
 	   
 	    FindUserroleByIdOutput output=mapper.userroleEntityToFindUserroleByIdOutput(foundUserrole); 
		return output;
	}
    //User
	// ReST API Call - GET /userrole/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetUserOutput getUser(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		UserEntity re = _userroleManager.getUser(userroleId);
		return mapper.userEntityToGetUserOutput(re, foundUserrole);
	}
	
    //Role
	// ReST API Call - GET /userrole/1/role
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetRoleOutput getRole(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.findById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		RoleEntity re = _userroleManager.getRole(userroleId);
		return mapper.roleEntityToGetRoleOutput(re, foundUserrole);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindUserroleByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserroleEntity> foundUserrole = _userroleManager.findAll(search(search), pageable);
		List<UserroleEntity> userroleList = foundUserrole.getContent();
		Iterator<UserroleEntity> userroleIterator = userroleList.iterator(); 
		List<FindUserroleByIdOutput> output = new ArrayList<>();

		while (userroleIterator.hasNext()) {
			output.add(mapper.userroleEntityToFindUserroleByIdOutput(userroleIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QUserroleEntity userrole= QUserroleEntity.userroleEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(userrole, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
    	list.get(i).replace("%20","").trim().equals("userId")||
		list.get(i).replace("%20","").trim().equals("role") ||
		list.get(i).replace("%20","").trim().equals("roleId") ||
		list.get(i).replace("%20","").trim().equals("user"))) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QUserroleEntity userrole, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(userrole.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(userrole.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public UserroleId parseUserroleKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		UserroleId userroleId = new UserroleId();
		
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
		
		userroleId.setRoleId(Long.valueOf(keyMap.get("roleId")));
        userroleId.setUserId(Long.valueOf(keyMap.get("userId")));
		return userroleId;
		
	}	
	
}



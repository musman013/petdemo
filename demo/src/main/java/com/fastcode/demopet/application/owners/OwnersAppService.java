package com.fastcode.demopet.application.owners;

import com.fastcode.demopet.application.authorization.user.IUserAppService;
import com.fastcode.demopet.application.authorization.user.IUserMapper;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserOutput;
import com.fastcode.demopet.application.authorization.user.dto.UserProfile;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.authorization.userrole.dto.CreateUserroleInput;
import com.fastcode.demopet.application.owners.dto.*;
import com.fastcode.demopet.domain.owners.IOwnersManager;
import com.fastcode.demopet.domain.model.QOwnersEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.authorization.role.IRoleManager;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class OwnersAppService implements IOwnersAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private IOwnersManager _ownersManager;

	@Autowired
	private IUserManager _userManager;
	
	@Autowired
	private IRoleManager _roleManager;
	
	@Autowired 
	private UserroleAppService _userroleAppService;
	
	@Autowired 
	private UserAppService _userAppService;

	@Autowired
	private IUserMapper _userMapper;

	@Autowired
	private IOwnersMapper mapper;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateOwnersOutput create(CreateOwnersInput input) {

		UserEntity user = _userManager.create(_userMapper.createUserInputToUserEntity(input));

		OwnersEntity owners = mapper.createOwnersInputToOwnersEntity(input);
		owners.setUser(user);
		
		assignOwnerRole(owners.getId());
		
		OwnersEntity createdOwners = _ownersManager.create(owners);
		return mapper.ownersEntityAndUserEntityToCreateOwnersOutput(createdOwners,user);
	}
	
	public void assignOwnerRole(Long userId)
	{
		RoleEntity role = _roleManager.findByRoleName("ROLE_Owner");
		CreateUserroleInput input = new CreateUserroleInput();
		input.setRoleId(role.getId());
		input.setUserId(userId);
		_userroleAppService.create(input);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateOwnersOutput update(Long  ownersId, UpdateOwnersInput input) {

		UserEntity user = _userMapper.updateUserInputToUserEntity(input);
        FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(Long.valueOf(ownersId));
	
        user.setVersion(currentUser.getVersion());
        user.setPassword(currentUser.getPassword());
        user=_userManager.update(user);
		OwnersEntity owners = mapper.updateOwnersInputToOwnersEntity(input);
		OwnersEntity updatedOwners = _ownersManager.update(owners);

		return mapper.ownersEntityAndUserEntityToUpdateOwnersOutput(updatedOwners,user);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long ownersId) {

		OwnersEntity existing = _ownersManager.findById(ownersId); 
		_ownersManager.delete(existing);
		_userroleAppService.deleteByUserId(existing.getUser().getId());
		_userManager.delete(existing.getUser());
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindOwnersByIdOutput findById(Long ownersId) {

		OwnersEntity foundOwners = _ownersManager.findById(ownersId);
		if (foundOwners == null)  
			return null ; 

		FindOwnersByIdOutput output=mapper.ownersEntityAndUserEntityToFindOwnersByIdOutput(foundOwners,foundOwners.getUser()); 
		return output;
	}
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindOwnersByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<OwnersEntity> foundOwners = _ownersManager.findAll(search(search), pageable);
		List<OwnersEntity> ownersList = foundOwners.getContent();
		Iterator<OwnersEntity> ownersIterator = ownersList.iterator(); 
		List<FindOwnersByIdOutput> output = new ArrayList<>();

		while (ownersIterator.hasNext()) {
			OwnersEntity owner = ownersIterator.next();
			output.add(mapper.ownersEntityAndUserEntityToFindOwnersByIdOutput(owner, owner.getUser()));
		}
		return output;
	}

	public OwnerProfile getProfile(FindOwnersByIdOutput owner)
	{
		return mapper.findOwnersByIdOutputToOwnerProfile(owner);
	}

	public OwnerProfile updateOwnerProfile(FindUserWithAllFieldsByIdOutput user, OwnerProfile ownerProfile)
	{
		UpdateOwnersInput ownerInput = mapper.findUserWithAllFieldsByIdOutputAndOwnerProfileToUpdateOwnerInput(user, ownerProfile);
		ownerInput.setVersion(user.getVersion());
		UpdateOwnersOutput output = update(user.getId(),ownerInput);

		return mapper.updateOwnerOutputToOwnerProfile(output);
	}


	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QOwnersEntity owners= QOwnersEntity.ownersEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(owners, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("address") ||
					list.get(i).replace("%20","").trim().equals("city") ||
					list.get(i).replace("%20","").trim().equals("userId") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("pets")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchKeyValuePair(QOwnersEntity owners, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
			if(details.getKey().replace("%20","").trim().equals("address")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(owners.address.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(owners.address.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(owners.address.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("city")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(owners.city.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(owners.city.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(owners.city.ne(details.getValue().getSearchValue()));
			}
			//            if(details.getKey().replace("%20","").trim().equals("firstName")) {
			//				if(details.getValue().getOperator().equals("contains"))
			//					builder.and(owners.firstName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
			//				else if(details.getValue().getOperator().equals("equals"))
			//					builder.and(owners.firstName.eq(details.getValue().getSearchValue()));
			//				else if(details.getValue().getOperator().equals("notEqual"))
			//					builder.and(owners.firstName.ne(details.getValue().getSearchValue()));
			//			}
			//            if(details.getKey().replace("%20","").trim().equals("lastName")) {
			//				if(details.getValue().getOperator().equals("contains"))
			//					builder.and(owners.lastName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
			//				else if(details.getValue().getOperator().equals("equals"))
			//					builder.and(owners.lastName.eq(details.getValue().getSearchValue()));
			//				else if(details.getValue().getOperator().equals("notEqual"))
			//					builder.and(owners.lastName.ne(details.getValue().getSearchValue()));
			//			}
			//            if(details.getKey().replace("%20","").trim().equals("telephone")) {
			//				if(details.getValue().getOperator().equals("contains"))
			//					builder.and(owners.telephone.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
			//				else if(details.getValue().getOperator().equals("equals"))
			//					builder.and(owners.telephone.eq(details.getValue().getSearchValue()));
			//				else if(details.getValue().getOperator().equals("notEqual"))
			//					builder.and(owners.telephone.ne(details.getValue().getSearchValue()));
			//			}

			for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
				if(joinCol != null && joinCol.getKey().equals("userId")) {
					builder.and(owners.user.id.eq(Long.parseLong(joinCol.getValue())));
				}
			}
		}
		return builder;
	}


	public Map<String,String> parsePetsJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("ownerId", keysString);
		return joinColumnMap;
	}


}



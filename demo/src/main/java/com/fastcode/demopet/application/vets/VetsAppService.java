package com.fastcode.demopet.application.vets;

import com.fastcode.demopet.application.authorization.role.dto.FindRoleByNameOutput;
import com.fastcode.demopet.application.authorization.user.IUserMapper;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.authorization.userrole.dto.CreateUserroleInput;
import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.domain.vets.IVetsManager;
import com.fastcode.demopet.domain.authorization.role.IRoleManager;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.QVetsEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.commons.search.*;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class VetsAppService implements IVetsAppService {

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;

	@Autowired
	private IVetsManager _vetsManager;

	@Autowired
	private IVetsMapper mapper;

	@Autowired
	private IUserManager _userManager;
	
	@Autowired
	private IRoleManager _roleManager;

	@Autowired
	private IUserMapper _userMapper;
	
	@Autowired
	private UserroleAppService _userroleAppService;
	
	@Autowired
	private UserAppService _userAppService;

	@Transactional(propagation = Propagation.REQUIRED)
	public CreateVetsOutput create(CreateVetsInput input) {

		VetsEntity vets = mapper.createVetsInputToVetsEntity(input);
		UserEntity user = _userManager.create(_userMapper.createUserInputToUserEntity(input));
		assignVetRole(user.getId());
		
		vets.setUser(user);

		VetsEntity createdVets = _vetsManager.create(vets);
		return mapper.vetsEntityAndUserEntityToCreateVetsOutput(createdVets, user);
	}
	
	public void assignVetRole(Long userId)
	{
		RoleEntity role = _roleManager.findByRoleName("ROLE_Vet");
		CreateUserroleInput input = new CreateUserroleInput();
		input.setRoleId(role.getId());
		input.setUserId(userId);
		_userroleAppService.create(input);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateVetsOutput update(Long vetsId, UpdateVetsInput input) {	
		
		VetsEntity vets = mapper.updateVetsInputToVetsEntity(input);
		FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(Long.valueOf(vetsId));
		
		UserEntity user = _userMapper.updateUserInputToUserEntity(input);
        user.setVersion(currentUser.getVersion());
        user.setPassword(currentUser.getPassword());
        user=_userManager.update(user);
        
		VetsEntity updatedVets = _vetsManager.update(vets);

		return mapper.vetsEntityAndUserEntityToUpdateVetsOutput(updatedVets, user);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long vetsId) {

		VetsEntity existing = _vetsManager.findById(vetsId) ; 
		_vetsManager.delete(existing);
		_userroleAppService.deleteByUserId(existing.getUser().getId());
		_userManager.delete(existing.getUser());
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindVetsByIdOutput findById(Long vetsId) {

		VetsEntity foundVets = _vetsManager.findById(vetsId);
		if (foundVets == null)  
			return null ; 

		FindVetsByIdOutput output=mapper.vetsEntityAndUserEntityToFindVetsByIdOutput(foundVets, foundVets.getUser()); 
		return output;
	}

	public VetProfile getProfile(FindVetsByIdOutput vet)
	{
		return mapper.findVetsByIdOutputToVetProfile(vet);
	}
	
	public VetProfile updateVetProfile(FindUserWithAllFieldsByIdOutput user, VetProfile vetProfile)
	{
		UpdateVetsInput userInput = mapper.findUserWithAllFieldsByIdOutputAndVetProfileToUpdateVetInput(user, vetProfile);
		userInput.setVersion(user.getVersion());
		UpdateVetsOutput output = update(user.getId(),userInput);
		
		return mapper.updateVetsOutputToVetProfile(output);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindVetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<VetsEntity> foundVets = _vetsManager.findAll(search(search), pageable);
		List<VetsEntity> vetsList = foundVets.getContent();
		Iterator<VetsEntity> vetsIterator = vetsList.iterator(); 
		List<FindVetsByIdOutput> output = new ArrayList<>();

		while (vetsIterator.hasNext()) {
			VetsEntity vet = vetsIterator.next();
			output.add(mapper.vetsEntityAndUserEntityToFindVetsByIdOutput(vet, vet.getUser()));
		}
		return output;
	}



	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QVetsEntity vets= QVetsEntity.vetsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(vets, map,search.getJoinColumns());
		}
		return null;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
					list.get(i).replace("%20","").trim().equals("userId") ||
					list.get(i).replace("%20","").trim().equals("userName") ||
					list.get(i).replace("%20","").trim().equals("id") ||
					list.get(i).replace("%20","").trim().equals("vetspecialties")
					)) 
			{
				throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}

	public BooleanBuilder searchKeyValuePair(QVetsEntity vets, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

				for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		            if(details.getKey().replace("%20","").trim().equals("userName")) {
						if(details.getValue().getOperator().equals("contains"))
							builder.and(vets.user.userName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
						else if(details.getValue().getOperator().equals("equals"))
							builder.and(vets.user.userName.eq(details.getValue().getSearchValue()));
						else if(details.getValue().getOperator().equals("notEqual"))
							builder.and(vets.user.userName.ne(details.getValue().getSearchValue()));
					}
				}
		//            if(details.getKey().replace("%20","").trim().equals("lastName")) {
		//				if(details.getValue().getOperator().equals("contains"))
		//					builder.and(vets.lastName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
		//				else if(details.getValue().getOperator().equals("equals"))
		//					builder.and(vets.lastName.eq(details.getValue().getSearchValue()));
		//				else if(details.getValue().getOperator().equals("notEqual"))
		//					builder.and(vets.lastName.ne(details.getValue().getSearchValue()));
		//			}
		//		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
			if(joinCol != null && joinCol.getKey().equals("userId")) {
				builder.and(vets.user.id.eq(Long.parseLong(joinCol.getValue())));
			}
		}
		return builder;
	}


	public Map<String,String> parseVetSpecialtiesJoinColumn(String keysString) {

		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("vetId", keysString);
		return joinColumnMap;
	}


}



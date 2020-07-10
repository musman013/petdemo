package com.fastcode.demopet.application.owners;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.demopet.domain.owners.*;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.application.authorization.user.IUserMapper;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.CreateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.owners.dto.*;
import com.fastcode.demopet.application.processmanagement.ActIdUserMapper;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.domain.model.QOwnersEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserpreferenceEntity;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.authorization.userpreference.UserpreferenceManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.pets.PetsManager;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class OwnersAppServiceTest {

	@InjectMocks
	@Spy
	OwnersAppService _appService;

	@Mock
	private OwnersManager _ownersManager;
	
	@Mock
	private UserManager _userManager;
	
	@Mock
	private UserAppService _userAppService;
	
	@Mock
	private UserroleAppService _userroleAppService;

	@Mock
	private UserpreferenceManager _userpreferenceManager;

	@Mock
	private PetsManager  _petsManager;

	@Mock
	private IOwnersMapper _mapper;

	@Mock
	private IUserMapper _userMapper;
	
	@Mock
 	private ActIdUserMapper actIdUserMapper;
 	
 	@Mock
 	private FlowableIdentityService idmIdentityService;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;


	private static Long ID=15L;


	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void findOwnersById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(null);
		Mockito.when(_userpreferenceManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}

	@Test
	public void findOwnersById_IdIsNotNullAndIdExists_ReturnOwners() {

		OwnersEntity owners = mock(OwnersEntity.class);
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(owners);
		Mockito.when(_userpreferenceManager.findById(anyLong())).thenReturn(userPreference);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.ownersEntityAndUserEntityToFindOwnersByIdOutput(owners, owners.getUser(), userPreference));
	}

	@Test 
	public void createOwners_OwnersIsNotNullAndOwnersDoesNotExist_StoreOwners() { 

		OwnersEntity ownersEntity = mock(OwnersEntity.class); 
		UserEntity userEntity = mock(UserEntity.class);
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);

		CreateOwnersInput owners = new CreateOwnersInput();

		Mockito.when(_userMapper.createUserInputToUserEntity(any(CreateUserInput.class))).thenReturn(userEntity);
		Mockito.when(_mapper.createOwnersInputToOwnersEntity(any(CreateOwnersInput.class))).thenReturn(ownersEntity); 
		
		ActIdUserEntity actIdUser = mock (ActIdUserEntity.class);
 		Mockito.when(actIdUserMapper.createUsersEntityToActIdUserEntity(any(UserEntity.class))).thenReturn(actIdUser);
 		doNothing().when(idmIdentityService).createUser(any(UserEntity.class),any(ActIdUserEntity.class));
	 	doNothing().when(_appService).assignOwnerRole(anyLong());
	 	
	 	Mockito.when(_userManager.create(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.when(_ownersManager.create(any(OwnersEntity.class))).thenReturn(ownersEntity);
		Mockito.when(_userAppService.createDefaultUserPreference(any(UserEntity.class))).thenReturn(userPreference); 
		
		Assertions.assertThat(_appService.create(owners)).isEqualTo(_mapper.ownersEntityAndUserEntityToCreateOwnersOutput(ownersEntity,  ownersEntity.getUser(),userPreference)); 
	} 

	@Test
	public void updateOwners_OwnersIdIsNotNullAndIdExists_ReturnUpdatedOwners() {

		OwnersEntity ownersEntity = mock(OwnersEntity.class);
		UserEntity userEntity = mock(UserEntity.class);
		UpdateOwnersInput owners= mock(UpdateOwnersInput.class);

		FindUserWithAllFieldsByIdOutput userOutputDto = new FindUserWithAllFieldsByIdOutput();

		Mockito.when(_userMapper.updateUserInputToUserEntity(any(UpdateUserInput.class))).thenReturn(userEntity);
		Mockito.when(_mapper.updateOwnersInputToOwnersEntity(any(UpdateOwnersInput.class))).thenReturn(ownersEntity);
		ActIdUserEntity actIdUser = mock (ActIdUserEntity.class); 
 		Mockito.when(_userManager.findById(anyLong())).thenReturn(userEntity);
 		Mockito.when(actIdUserMapper.createUsersEntityToActIdUserEntity(any(UserEntity.class))).thenReturn(actIdUser);
 		Mockito.when( _userAppService.findWithAllFieldsById(anyLong())).thenReturn(userOutputDto);
 		
 		Mockito.when(_userManager.update(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.when(_ownersManager.update(any(OwnersEntity.class))).thenReturn(ownersEntity);
		Assertions.assertThat(_appService.update(ID,owners)).isEqualTo(_mapper.ownersEntityAndUserEntityToUpdateOwnersOutput(ownersEntity, ownersEntity.getUser()));
	}

	@Test
	public void deleteOwners_OwnersIsNotNullAndOwnersExists_OwnersRemoved() {

		OwnersEntity owners = new OwnersEntity();
		UserEntity userEntity = new UserEntity();
		owners.setUser(userEntity);
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(owners);
	
		doNothing().when(_userroleAppService).deleteByUserId(anyLong());
		doNothing().when(_userAppService).delete(anyLong());
		doNothing().when(idmIdentityService).deleteUser(any(String.class));
		_appService.delete(ID); 
		verify(_ownersManager).delete(owners);
	}

	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<OwnersEntity> list = new ArrayList<>();
		Page<OwnersEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindOwnersByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		//		search.setType(1);
		//		search.setValue("xyz");
		//		search.setOperator("equals");
		Mockito.doReturn(new BooleanBuilder()).when(_appService).search(any(SearchCriteria.class));
	//	Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_ownersManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}

	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<OwnersEntity> list = new ArrayList<>();
		OwnersEntity owners = mock(OwnersEntity.class);
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);

		list.add(owners);
		Page<OwnersEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindOwnersByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		//		search.setType(1);
		//		search.setValue("xyz");
		//		search.setOperator("equals");
		output.add(_mapper.ownersEntityAndUserEntityToFindOwnersByIdOutput(owners, owners.getUser(), userPreference));
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_ownersManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}

	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QOwnersEntity owners = QOwnersEntity.ownersEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap<>();
		map.put("address",searchFields);
		Map<String,String> searchMap = new HashMap<>();
		searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(owners.address.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(owners,map,searchMap)).isEqualTo(builder);
	}

	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}

	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("address");
		list.add("city");
		list.add("userName");
		list.add("userId");
		list.add("pets");
		list.add("id");
		_appService.checkProperties(list);
	}

	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {

		Map<String,SearchFields> map = new HashMap<>();
		QOwnersEntity owners = QOwnersEntity.ownersEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("address");
		fields.setOperator("equals");
		fields.setSearchValue("xyz");
		fieldsList.add(fields);
		search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(owners.address.eq("xyz"));
		Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QOwnersEntity.class), any(HashMap.class), any(HashMap.class));

		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}

	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}

	@Test
	public void ParsePetsJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("ownerId", keyString);
		Assertions.assertThat(_appService.parsePetsJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}


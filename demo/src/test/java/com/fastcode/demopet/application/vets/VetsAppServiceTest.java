package com.fastcode.demopet.application.vets;

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

import com.fastcode.demopet.domain.vets.*;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.application.authorization.user.IUserMapper;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.CreateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.processmanagement.ActIdUserMapper;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.domain.authorization.user.UserManager;
import com.fastcode.demopet.domain.authorization.userpreference.UserpreferenceManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.QVetsEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserpreferenceEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;
import com.fastcode.demopet.domain.model.VetSpecialtiesEntity;
import com.fastcode.demopet.domain.vetspecialties.VetSpecialtiesManager;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VetsAppServiceTest {

	@InjectMocks
	@Spy
	VetsAppService _appService;

	@Mock
	private VetsManager _vetsManager;
	
	@Mock
	private UserAppService _userAppService;

	@Mock
	private VetSpecialtiesManager  _vetSpecialtiesManager;

	@Mock
	private IUserMapper _userMapper;
	
	@Mock
	private IVetsMapper _mapper;

	@Mock
	private Logger loggerMock;
	
	@Mock
	private UserManager _userManager;

	@Mock
	private UserroleAppService _userroleAppService;

	@Mock
	private UserpreferenceManager _userpreferenceManager;
	
	@Mock
 	private ActIdUserMapper actIdUserMapper;
 	
 	@Mock
 	private FlowableIdentityService idmIdentityService;

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
	public void findVetsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(null);
		Mockito.when(_userpreferenceManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}

	@Test
	public void findVetsById_IdIsNotNullAndIdExists_ReturnVets() {

		VetsEntity vets = mock(VetsEntity.class);
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);

		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(vets);
		Mockito.when(_userpreferenceManager.findById(anyLong())).thenReturn(userPreference);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.vetsEntityAndUserEntityToFindVetsByIdOutput(vets, vets.getUser(), userPreference));

	}

	@Test 
	public void createVets_VetsIsNotNullAndVetsDoesNotExist_StoreVets() { 

		VetsEntity vetsEntity = mock(VetsEntity.class); 
		UserEntity userEntity = mock(UserEntity.class);
		CreateVetsInput vets = new CreateVetsInput();
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);

		Mockito.when(_userMapper.createUserInputToUserEntity(any(CreateUserInput.class))).thenReturn(userEntity);
		Mockito.when(_mapper.createVetsInputToVetsEntity(any(CreateVetsInput.class))).thenReturn(vetsEntity); 
		
		ActIdUserEntity actIdUser = mock (ActIdUserEntity.class);
 		Mockito.when(actIdUserMapper.createUsersEntityToActIdUserEntity(any(UserEntity.class))).thenReturn(actIdUser);
 		doNothing().when(idmIdentityService).createUser(any(UserEntity.class),any(ActIdUserEntity.class));
	 	doNothing().when(_appService).assignVetRole(anyLong());
	 	
	 	Mockito.when(_userManager.create(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.when(_vetsManager.create(any(VetsEntity.class))).thenReturn(vetsEntity);
        Mockito.when(_userAppService.createDefaultUserPreference(any(UserEntity.class))).thenReturn(userPreference); 
		
		Assertions.assertThat(_appService.create(vets)).isEqualTo(_mapper.vetsEntityAndUserEntityToCreateVetsOutput(vetsEntity, vetsEntity.getUser(),userPreference)); 
	
	} 
	@Test
	public void updateVets_VetsIdIsNotNullAndIdExists_ReturnUpdatedVets() {

		VetsEntity vetsEntity = mock(VetsEntity.class);
		UserEntity userEntity = mock(UserEntity.class);
		
		UpdateVetsInput vets= mock(UpdateVetsInput.class);

		FindUserWithAllFieldsByIdOutput userOutputDto = new FindUserWithAllFieldsByIdOutput();

		Mockito.when(_userMapper.updateUserInputToUserEntity(any(UpdateUserInput.class))).thenReturn(userEntity);
		Mockito.when(_mapper.updateVetsInputToVetsEntity(any(UpdateVetsInput.class))).thenReturn(vetsEntity);
		ActIdUserEntity actIdUser = mock (ActIdUserEntity.class); 
 		Mockito.when(_userManager.findById(anyLong())).thenReturn(userEntity);
 		Mockito.when(actIdUserMapper.createUsersEntityToActIdUserEntity(any(UserEntity.class))).thenReturn(actIdUser);
 		Mockito.when( _userAppService.findWithAllFieldsById(anyLong())).thenReturn(userOutputDto);
 		
 		Mockito.when(_userManager.update(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.when(_vetsManager.update(any(VetsEntity.class))).thenReturn(vetsEntity);
		Assertions.assertThat(_appService.update(ID,vets)).isEqualTo(_mapper.vetsEntityAndUserEntityToUpdateVetsOutput(vetsEntity, vetsEntity.getUser()));
	}

	@Test
	public void deleteVets_VetsIsNotNullAndVetsExists_VetsRemoved() {

		VetsEntity vets= new VetsEntity();
		
		UserEntity userEntity = new UserEntity();
		vets.setUser(userEntity);
		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(vets);
		doNothing().when(_userroleAppService).deleteByUserId(anyLong());
		doNothing().when(_userAppService).delete(anyLong());
		doNothing().when(idmIdentityService).deleteUser(any(String.class));

		_appService.delete(ID); 
		verify(_vetsManager).delete(vets);
	}

	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<VetsEntity> list = new ArrayList<>();
		Page<VetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		//		search.setType(1);
		//		search.setValue("xyz");
		//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_vetsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}

	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<VetsEntity> list = new ArrayList<>();
		VetsEntity vets = mock(VetsEntity.class);
		UserpreferenceEntity userPreference = mock(UserpreferenceEntity.class);

		list.add(vets);
		Page<VetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVetsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		
		output.add(_mapper.vetsEntityAndUserEntityToFindVetsByIdOutput(vets, vets.getUser(), userPreference));
		Mockito.doReturn(new BooleanBuilder()).when(_appService).search(any(SearchCriteria.class));
	//	Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_vetsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}

	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QVetsEntity vets = QVetsEntity.vetsEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap<>();
		map.put("userName",searchFields);
		Map<String,String> searchMap = new HashMap<>();
		searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(vets.user.userName.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(vets,map,searchMap)).isEqualTo(builder);
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
		list.add("userName");
		list.add("userId");
		list.add("id");
		list.add("vetspecialties");
		_appService.checkProperties(list);
	}

	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {

		Map<String,SearchFields> map = new HashMap<>();
		QVetsEntity vets = QVetsEntity.vetsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("firstName");
		fields.setOperator("equals");
		fields.setSearchValue("xyz");
		fieldsList.add(fields);
		search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(vets.user.firstName.eq("xyz"));
		Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QVetsEntity.class), any(HashMap.class), any(HashMap.class));

		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}

	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}

	@Test
	public void ParseVetSpecialtiesJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("vetId", keyString);
		Assertions.assertThat(_appService.parseVetSpecialtiesJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}


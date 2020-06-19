package com.fastcode.demo.domain.authorization.userrole;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.demo.domain.model.UserroleEntity;
import com.fastcode.demo.domain.irepository.IUserRepository;
import com.fastcode.demo.domain.model.UserEntity;
import com.fastcode.demo.domain.irepository.IRoleRepository;
import com.fastcode.demo.domain.model.RoleEntity;
import com.fastcode.demo.domain.model.UserroleId;
import com.fastcode.demo.domain.irepository.IUserroleRepository;
import com.fastcode.demo.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserroleManagerTest {

	@InjectMocks
	UserroleManager _userroleManager;
	
	@Mock
	IUserroleRepository  _userroleRepository;
    
    @Mock
	IUserRepository  _userRepository;
    
    @Mock
	IRoleRepository  _roleRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	@Mock
	private UserroleId userroleId;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_userroleManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findUserroleById_IdIsNotNullAndIdExists_ReturnUserrole() {
		UserroleEntity userrole =mock(UserroleEntity.class);

        Optional<UserroleEntity> dbUserrole = Optional.of((UserroleEntity) userrole);
		Mockito.<Optional<UserroleEntity>>when(_userroleRepository.findById(any(UserroleId.class))).thenReturn(dbUserrole);
		Assertions.assertThat(_userroleManager.findById(userroleId)).isEqualTo(userrole);
	}

	@Test 
	public void findUserroleById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<UserroleEntity>>when(_userroleRepository.findById(any(UserroleId.class))).thenReturn(Optional.empty());
		Assertions.assertThat(_userroleManager.findById(userroleId)).isEqualTo(null);
	}
	
	@Test
	public void createUserrole_UserroleIsNotNullAndUserroleDoesNotExist_StoreUserrole() {

		UserroleEntity userrole =mock(UserroleEntity.class);
		Mockito.when(_userroleRepository.save(any(UserroleEntity.class))).thenReturn(userrole);
		Assertions.assertThat(_userroleManager.create(userrole)).isEqualTo(userrole);
	}

	@Test
	public void deleteUserrole_UserroleExists_RemoveUserrole() {

		UserroleEntity userrole =mock(UserroleEntity.class);
		_userroleManager.delete(userrole);
		verify(_userroleRepository).delete(userrole);
	}

	@Test
	public void updateUserrole_UserroleIsNotNullAndUserroleExists_UpdateUserrole() {
		
		UserroleEntity userrole =mock(UserroleEntity.class);
		Mockito.when(_userroleRepository.save(any(UserroleEntity.class))).thenReturn(userrole);
		Assertions.assertThat(_userroleManager.update(userrole)).isEqualTo(userrole);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<UserroleEntity> userrole = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_userroleRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(userrole);
		Assertions.assertThat(_userroleManager.findAll(predicate,pageable)).isEqualTo(userrole);
	}
	
    //User
	@Test
	public void getUser_if_UserroleIdIsNotNull_returnUser() {

		UserroleEntity userrole = mock(UserroleEntity.class);
		UserEntity user = mock(UserEntity.class);
		
        Optional<UserroleEntity> dbUserrole = Optional.of((UserroleEntity) userrole);
		Mockito.<Optional<UserroleEntity>>when(_userroleRepository.findById(any(UserroleId.class))).thenReturn(dbUserrole);
		Mockito.when(userrole.getUser()).thenReturn(user);
		Assertions.assertThat(_userroleManager.getUser(userroleId)).isEqualTo(user);

	}
	
    //Role
	@Test
	public void getRole_if_UserroleIdIsNotNull_returnRole() {

		UserroleEntity userrole = mock(UserroleEntity.class);
		RoleEntity role = mock(RoleEntity.class);
		
        Optional<UserroleEntity> dbUserrole = Optional.of((UserroleEntity) userrole);
		Mockito.<Optional<UserroleEntity>>when(_userroleRepository.findById(any(UserroleId.class))).thenReturn(dbUserrole);
		Mockito.when(userrole.getRole()).thenReturn(role);
		Assertions.assertThat(_userroleManager.getRole(userroleId)).isEqualTo(role);

	}
	
}

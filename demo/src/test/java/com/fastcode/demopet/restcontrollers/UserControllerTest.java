package com.fastcode.demopet.restcontrollers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.security.JWTAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.CreateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;
import com.fastcode.demopet.application.authorization.userpermission.UserpermissionAppService;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.processmanagement.ActIdUserMapper;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.domain.irepository.IActIdUserRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class UserControllerTest {

	private static final String DEFAULT_USER_NAME = "U1";
	
	@Autowired
    private SortHandlerMethodArgumentResolver sortArgumentResolver;
	
	@Autowired 
	private IUserRepository user_repository;

	@SpyBean
	private UserAppService userAppService;
	
	@SpyBean
	private UserpermissionAppService userpermissionAppService;
	
	@SpyBean
	private UserroleAppService userroleAppService;
	
	@SpyBean
	private LoggingHelper logHelper;
	
	@SpyBean
	private PasswordEncoder pEncoder;
	
	@SpyBean
    private FlowableIdentityService idmIdentityService;
 
	@SpyBean
    private ActIdUserMapper actIdUserMapper;
     
	@Autowired
 	private IActIdUserRepository actIdUserRepo;
	 
	@SpyBean
	private JWTAppService jwtAppService;
	
	@Mock
	private Logger loggerMock;
	
	private UserEntity user;
	
	private MockMvc mvc;

	
	@Autowired
	EntityManagerFactory emf;
	
    static EntityManagerFactory emfs;
	
	@PostConstruct
	public void init() {
	this.emfs = emf;
	}

	@AfterClass
	public static void cleanup() {
		EntityManager em = emfs.createEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
		em.createNativeQuery("truncate table sample.f_user").executeUpdate();
		em.createNativeQuery("truncate table sample.act_id_user").executeUpdate();
		em.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
		
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}
	
	public static UserEntity createEntity() {
		UserEntity user = new UserEntity();
		user.setUserName(DEFAULT_USER_NAME);
		user.setId(1L);
		user.setIsActive(true);
		user.setPassword("secret");
		user.setFirstName("U1");
		user.setLastName("11");
		user.setEmailAddress("u11@g.com");
		
		return user;
	}

	public static CreateUserInput createUserInput() {
		CreateUserInput user = new CreateUserInput();
		
		user.setUserName("newUser");
		user.setIsActive(true);
		user.setPassword("secret");
		user.setFirstName("U22");
		user.setLastName("122");
		user.setEmailAddress("u122@g.com");

		return user;
	}
	
	public static UserEntity createNewEntity() {
		UserEntity user = new UserEntity();
		user.setId(2L);
		user.setIsActive(false);
		user.setPassword("secret");
		user.setUserName("U25");
		user.setFirstName("U25");
		user.setLastName("125");
		user.setEmailAddress("u125@g.com");

		return user;
	}

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserController userController = new UserController(userAppService, userpermissionAppService, userroleAppService, pEncoder, jwtAppService, logHelper);
        
        when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		
		
        this.mvc = MockMvcBuilders.standaloneSetup(userController)
        		.setCustomArgumentResolvers(sortArgumentResolver)
        		.setControllerAdvice()
                .build();
        	
    }

	@Before
	public void initTest() {
	
		user = createEntity();
	
		List<UserEntity> list= user_repository.findAll();

	    if(!list.stream().anyMatch(item -> user.getUserName().equals(item.getUserName()))) {
		   user=user_repository.save(user);
		   ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(user);
		   List<ActIdUserEntity> actUserList = actIdUserRepo.findAll();
		  
		   if(!actUserList.stream().anyMatch(item -> actIdUser.getId().equals(item.getId()))) {
 			 idmIdentityService.createUser(user, actIdUser);
 			}

		}
	
	}
	
	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	//	UserEntity user = user_repository.save(createEntity());
		 mvc.perform(get("/user/" + user.getId())
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
		 
		 user_repository.delete(user);
		 
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user/21")
	    		  .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	
//	      mvc.perform(get("/user/21")
//	    		  .contentType(MediaType.APPLICATION_JSON))
//	      .andExpect(status().isOk()).hasCause(new EntityNotFoundException("There does not exist a user with a id=21"));
	
	}    
	@Test
	public void CreateUser_UserDoesNotExist_ReturnStatusOk() throws Exception {
	
	    Mockito.doReturn(null).when(userAppService).findByUserName(anyString());
        
		CreateUserInput user = createUserInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(user);
      
		 mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(json))
		  .andExpect(status().isOk());
		 
	//	 user_repository.delete(createNewEntity());
	//	 idmIdentityService.deleteUser(createNewEntity().getUserName());

	}  

	@Test
	public void CreateUser_UserAlreadyExists_ThrowEntityExistsException() throws Exception {

		FindUserByNameOutput output= new FindUserByNameOutput();
	    output.setId(3L);
	    output.setUserName("U23");
	    output.setFirstName("U23");
	    output.setLastName("123");
	    output.setEmailAddress("u123@g.com");
	    output.setIsActive(true);

	    Mockito.when(userAppService.findByUserName(anyString())).thenReturn(output);
        
	    CreateUserInput user = createUserInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(user);
       
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> mvc.perform(post("/user")
        		.contentType(MediaType.APPLICATION_JSON).content(json))
         .andExpect(status().isOk())).hasCause(new EntityExistsException("There already exists a user with a name=" + user.getUserName()));
      
	}    
	
	@Test
	public void DeleteUser_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {
	   
	    Mockito.doReturn(null).when(userAppService).findById(anyLong());
     	 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/user/21")
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a user with a id=21"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
		
		UserEntity user = user_repository.save(createNewEntity());
		Long id = user.getId();
		
		FindUserByIdOutput output= new FindUserByIdOutput();
	    output.setId(id.longValue());
	    output.setUserName("abc");
	    output.setFirstName("U33");
	    output.setLastName("133");
	    output.setEmailAddress("u133@g.com");
	    output.setIsActive(true);
	    
	    Mockito.when(userAppService.findById(anyLong())).thenReturn(output);
	    
	    ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(createNewEntity());
 		idmIdentityService.createUser(createNewEntity(), actIdUser);
 		
     	 mvc.perform(delete("/user/"+id.toString())
     			 .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNoContent());
     	 
     	
	}  
	
	
	@Test
	public void UpdateUser_UserDoesNotExist_ReturnStatusNotFound() throws Exception {
		
        Mockito.doReturn(null).when(userAppService).findWithAllFieldsById(anyLong());
        UpdateUserInput user = new UpdateUserInput();
        user.setId(1L);
        user.setUserName("U116");
		user.setFirstName("U116");
		user.setLastName("116");
		user.setEmailAddress("u116@g.com");
		user.setIsActive(false);
		
 		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(user);
     
//        mvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON).content(json))
//		  .andExpect(status().isNotFound());
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/user/99")
	    		  .contentType(MediaType.APPLICATION_JSON).content(json))
	      .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. User with id=99 not found."));
     
	}    
	
	@Test
	public void UpdateUser_UserExists_ReturnStatusOk() throws Exception {
		UserEntity ue= user_repository.save(createNewEntity());
		Long id = ue.getId();
		FindUserWithAllFieldsByIdOutput output= new FindUserWithAllFieldsByIdOutput();
	    output.setId(id.longValue());
	    output.setUserName("us1");
	    output.setFirstName(ue.getFirstName());
	    output.setLastName(ue.getLastName());
	    output.setEmailAddress(ue.getEmailAddress());
	    output.setIsActive(ue.getIsActive());
	    output.setPassword(ue.getPassword());
	    
		Mockito.doReturn(output).when(userAppService).findWithAllFieldsById(anyLong());
	    doNothing().when(jwtAppService).deleteAllUserTokens(anyString());  
	    
        UpdateUserInput user = new UpdateUserInput();
        user.setId(id.longValue());
		user.setUserName("us1");
		user.setFirstName("U1");
		user.setLastName("16");
		user.setEmailAddress("u16@g.com");
		user.setIsActive(true);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(user);
      
		ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(ue);
 		idmIdentityService.createUser(ue, actIdUser);
 		
        mvc.perform(put("/user/"+id).contentType(MediaType.APPLICATION_JSON).content(json))
	    .andExpect(status().isOk());

        UserEntity de= createEntity();
        de.setId(id);
        idmIdentityService.deleteUser(de.getUserName());
        user_repository.delete(de);
     
	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
		
		 mvc.perform(get("/user?search=id[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}    
	
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {
		 
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user?search=roleid[equals]=1&limit=10&offset=1")
				 .contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property roleid not found!"));
	
	}   
	
	@Test
	public void GetUserrole_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("roleid", "1");
		Mockito.when(userAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user/2/userrole?search=roleid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property roleid not found!"));
	
	}    
	
	@Test
	public void GetUserrole_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("userId", "1");
		Mockito.when(userAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/user/2/userrole?search=userId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUserrole_searchIsNotEmpty() throws Exception {
	
		Mockito.when(userAppService.parseUserroleJoinColumn(any(String.class))).thenReturn(null);
//		mvc.perform(get("/user/2/userrole?search=roleid[equals]=1&limit=10&offset=1")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user/2/userrole?search=roleid[equals]=1&limit=10&offset=1")
	    		  .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
    
	}    
	
	@Test
	public void GetUserpermission_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("userid", "1");
		Mockito.when(userAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user/2/userpermission?search=userid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property userid not found!"));
	
	}    
	
	@Test
	public void GetUserpermission_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("userId", "1");
		Mockito.when(userAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(joinCol);

		mvc.perform(get("/user/2/userpermission?search=userId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetUserpermission_searchIsNotEmpty() throws Exception {
	
		Mockito.when(userAppService.parseUserpermissionJoinColumn(any(String.class))).thenReturn(null);
//		mvc.perform(get("/user/2/userpermission?search=userid[equals]=1&limit=10&offset=1")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/user/2/userpermission?search=userid[equals]=1&limit=10&offset=1")
	    		  .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    
	
	
}

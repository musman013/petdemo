package com.fastcode.demopet.restcontrollers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

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
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.owners.OwnersAppService;
import com.fastcode.demopet.application.owners.dto.*;
import com.fastcode.demopet.domain.irepository.IOwnersRepository;
import com.fastcode.demopet.domain.irepository.IRoleRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.application.pets.PetsAppService;
import com.fastcode.demopet.application.vets.dto.CreateVetsInput;
import com.fastcode.demopet.application.vets.dto.FindVetsByIdOutput;
import com.fastcode.demopet.application.vets.dto.UpdateVetsInput;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = "spring.profiles.active=test")
public class OwnersControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IOwnersRepository owners_repository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private IUserRepository userRepository;

	@SpyBean
	private OwnersAppService ownersAppService;

	@SpyBean
	private PasswordEncoder pEncoder;

	@SpyBean
	private UserAppService userAppService;

	@SpyBean
	private PetsAppService petsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private OwnersEntity owners;

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
		em.createNativeQuery("truncate table sample.owners").executeUpdate();
		em.createNativeQuery("truncate table sample.role").executeUpdate();
		em.createNativeQuery("truncate table sample.f_user").executeUpdate();
		em.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
		
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}

	public static UserEntity createUserEntity() {
		UserEntity user = new UserEntity();
		user.setUserName("u1");
		user.setId(1L);
		user.setIsActive(true);
		user.setPassword("secret");
		user.setFirstName("U1");
		user.setLastName("11");
		user.setEmailAddress("u11@g.com");

		return user;
	}

	public OwnersEntity createEntity() {

		OwnersEntity owners = new OwnersEntity();
		owners.setAddress("1");
		owners.setCity("1");
		owners.setId(1L);
		owners.setUser(createUserEntity());

		return owners;
	}

	public RoleEntity createOwnerRole() {

		RoleEntity role = new RoleEntity();
		role.setName("ROLE_Owner");
		role.setDisplayName("r1");

		return roleRepository.save(role);
	}

	public CreateOwnersInput createOwnersInput() {

		CreateOwnersInput owners = new CreateOwnersInput();
		owners.setAddress("2");
		owners.setCity("2");
		owners.setFirstName("2");
		owners.setLastName("2");
  		owners.setUserName("u2");
		owners.setIsActive(true);
		owners.setPassword("secret");
		owners.setEmailAddress("u12@g.com");

		return owners;
	}

	//	public OwnersEntity createNewEntity() {
	//		OwnersEntity owners = new OwnersEntity();
	//  		owners.setAddress("3");
	//  		owners.setCity("3");
	//		owners.setId(3L);
	//		return owners;
	//	}
	//	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final OwnersController ownersController = new OwnersController(ownersAppService, petsAppService,
				logHelper, userAppService, pEncoder);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(ownersController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		owners= createEntity();
		List<OwnersEntity> list= owners_repository.findAll();

		if(!list.stream().anyMatch(item -> owners.getUser().getUserName().equals(item.getUser().getUserName()))) {
			UserEntity user = userRepository.save(createUserEntity());
			owners.setUser(user);
			owners.setId(user.getId());
			owners=owners_repository.save(owners);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/owners/" + owners.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		//		mvc.perform(get("/owners/111")
		//				.contentType(MediaType.APPLICATION_JSON))
		//		.andExpect(status().isNotFound());

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    
	
	@Test
	public void CreateOwners_OwnersDoesNotExist_ReturnStatusOk() throws Exception {

		Mockito.doReturn(null).when(userAppService).findByUserName(anyString());
		Mockito.doReturn(null).when(userAppService).findByEmailAddress(anyString());

		CreateOwnersInput owners = createOwnersInput();
		createOwnerRole();
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);

		mvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteOwners_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

		doReturn(null).when(ownersAppService).findById(111L);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/owners/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a owners with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {

		OwnersEntity entity =  new OwnersEntity();
		UserEntity user = createUserEntity();
		user.setUserName("kl");
		user= userRepository.save(user);
		entity.setId(user.getId());
		entity.setUser(user);
		entity = owners_repository.save(entity);

		FindOwnersByIdOutput output= new FindOwnersByIdOutput();
		output.setId(entity.getId());
		Mockito.when(ownersAppService.findById(entity.getId())).thenReturn(output);

		mvc.perform(delete("/owners/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateOwners_OwnersDoesNotExist_ReturnStatusNotFound() throws Exception {

		doReturn(null).when(ownersAppService).findById(111L);

		UpdateOwnersInput owners = new UpdateOwnersInput();
		owners.setAddress("111");
		owners.setCity("111");
		owners.setFirstName("111");
		owners.setId(99L);
		owners.setLastName("111");
		owners.setUserName("U116");
		owners.setEmailAddress("u116@g.com");
		owners.setIsActive(false);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);
		//		mvc.perform(put("/owners/111").contentType(MediaType.APPLICATION_JSON).content(json))
		//		.andExpect(status().isNotFound());
		//		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/owners/99")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Owners with id=99 not found."));


	}    

	@Test
	public void UpdateOwners_OwnersExists_ReturnStatusOk() throws Exception {
		
		UserEntity user = createUserEntity();
		user.setUserName("u7");
		user= userRepository.save(user);
		Long id = user.getId();
	
		FindOwnersByIdOutput output= new FindOwnersByIdOutput();
  		output.setId(id);
	    output.setUserName(user.getUserName());
	    output.setFirstName(user.getFirstName());
	    output.setLastName(user.getLastName());
	    output.setEmailAddress(user.getEmailAddress());
	    output.setIsActive(user.getIsActive());
	    Mockito.when(ownersAppService.findById(id)).thenReturn(output);
        
		UpdateOwnersInput owners = new UpdateOwnersInput();
		owners.setId(id);
		owners.setUserName("us1");
		owners.setFirstName("U1");
		owners.setLastName("16");
		owners.setEmailAddress("u16@g.com");
		owners.setIsActive(true);
		output.setAddress("abc");
		output.setCity("abc");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(owners);

		mvc.perform(put("/owners/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		OwnersEntity de = createEntity();
		de.setId(id);
		owners_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/owners?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners?search=ownersid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property ownersid not found!"));

	} 

	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("ownerId", "1");

		Mockito.when(ownersAppService.parsePetsJoinColumn("ownerId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners/1/pets?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));

	}    

	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("ownerId", "1");

		Mockito.when(ownersAppService.parsePetsJoinColumn("ownerId")).thenReturn(joinCol);
		
		mvc.perform(get("/owners/1/pets?search=ownerId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
	}  

	@Test
	public void GetPets_searchIsNotEmpty() throws Exception {

		Mockito.when(ownersAppService.parsePetsJoinColumn(anyString())).thenReturn(null);
//		mvc.perform(get("/owners/1/pets?search=ownerid[equals]=1&limit=10&offset=1")
//				.contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
//		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/owners/1/pets?search=ownerid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    


}

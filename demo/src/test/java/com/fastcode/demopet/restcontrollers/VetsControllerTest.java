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
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.vets.VetsAppService;
import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.domain.authorization.userrole.IUserroleManager;
import com.fastcode.demopet.domain.irepository.IRoleRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.irepository.IVetsRepository;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.application.vetspecialties.VetSpecialtiesAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class VetsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IVetsRepository vets_repository;
	
	@Autowired 
	private IUserRepository userRepository;
	
	@Autowired 
	private IRoleRepository roleRepository;
	
	@SpyBean
	private VetsAppService vetsAppService;
    
    @SpyBean
	private VetSpecialtiesAppService vetSpecialtiesAppService;
    
    @SpyBean
	private UserroleAppService userroleAppService;
    
    @SpyBean
	private IUserroleManager userroleManager;
    
    @SpyBean
    private PasswordEncoder pEncoder;
	
    @SpyBean
	private UserAppService userAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private VetsEntity vets;

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
		em.createNativeQuery("truncate table sample.vets").executeUpdate();
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

	public VetsEntity createEntity() {
	
		VetsEntity vets = new VetsEntity();
		vets.setUser(createUserEntity());
		vets.setId(1L);
		
		return vets;
	}

	public CreateVetsInput createVetsInput() {
	
	    CreateVetsInput vets = new CreateVetsInput();
  		vets.setFirstName("2");
  		vets.setLastName("2");
  		vets.setUserName("u2");
		vets.setIsActive(true);
		vets.setPassword("secret");
		vets.setFirstName("U2");
		vets.setLastName("12");
		vets.setEmailAddress("u12@g.com");
		
		return vets;
	}
	
	public RoleEntity createVetRole() {
		
		RoleEntity role = new RoleEntity();
		role.setName("ROLE_Vet");
		role.setDisplayName("r1");
		
		return roleRepository.save(role);
	}
//	public VetsEntity createNewEntity() {
//		VetsEntity vets = new VetsEntity();
//		UserEntity user = createUserEntity();
//		user.setUserName("u3");
//		vets.setUser(user);
//		vets.setId(3L);
//		return vets;
//	}
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final VetsController vetsController = new VetsController(vetsAppService,vetSpecialtiesAppService,
	    logHelper, userAppService, pEncoder);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(vetsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		vets= createEntity();
		List<VetsEntity> list= vets_repository.findAll();
		if(!list.stream().anyMatch(item -> vets.getUser().getUserName().equals(item.getUser().getUserName()))) {
			UserEntity user = userRepository.save(createUserEntity());
			vets.setUser(user);
			vets.setId(user.getId());
			vets=vets_repository.save(vets);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/vets/" + vets.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

//		mvc.perform(get("/vets/111")
//				.contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
		
		 org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets/111")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));


	}    
	@Test
	public void CreateVets_VetsDoesNotExist_ReturnStatusOk() throws Exception {
		
		Mockito.doReturn(null).when(userAppService).findByUserName(anyString());
		Mockito.doReturn(null).when(userAppService).findByEmailAddress(anyString());
        
		CreateVetsInput vets = createVetsInput();
		createVetRole();
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);

		mvc.perform(post("/vets").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteVets_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(vetsAppService).findById(111L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/vets/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a vets with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	    VetsEntity entity =  new VetsEntity();
		UserEntity user = createUserEntity();
		user.setUserName("kl");
		user= userRepository.save(user);
		entity.setId(user.getId());
		entity.setUser(user);
		entity = vets_repository.save(entity);
		FindVetsByIdOutput output= new FindVetsByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(vetsAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/vets/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateVets_VetsDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(vetsAppService).findById(111L);

		UpdateVetsInput vets = new UpdateVetsInput();
//  		vets.setFirstName("111");
//		vets.setId(111L);
//  		vets.setLastName("111");
  		vets.setId(1L);
        vets.setUserName("U116");
		vets.setFirstName("U116");
		vets.setLastName("116");
		vets.setEmailAddress("u116@g.com");
		vets.setIsActive(false);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);
//		mvc.perform(put("/vets/111").contentType(MediaType.APPLICATION_JSON).content(json))
//		.andExpect(status().isNotFound());
//		
		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/vets/99")
	    		  .contentType(MediaType.APPLICATION_JSON).content(json))
	      .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Vets with id=99 not found."));

	}    

	@Test
	public void UpdateVets_VetsExists_ReturnStatusOk() throws Exception {
		UserEntity user = createUserEntity();
		user.setUserName("u7");
		user= userRepository.save(user);
		Long id = user.getId();
	
		FindVetsByIdOutput output= new FindVetsByIdOutput();
  		output.setId(id);
	    output.setUserName(user.getUserName());
	    output.setFirstName(user.getFirstName());
	    output.setLastName(user.getLastName());
	    output.setEmailAddress(user.getEmailAddress());
	    output.setIsActive(user.getIsActive());
	    Mockito.when(vetsAppService.findById(id)).thenReturn(output);
        
		UpdateVetsInput vets = new UpdateVetsInput();
  		vets.setId(id);
		vets.setUserName("us1");
		vets.setFirstName("U1");
		vets.setLastName("16");
		vets.setEmailAddress("u16@g.com");
		vets.setIsActive(true);
  		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vets);
	
		mvc.perform(put("/vets/" + id).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		VetsEntity de = createEntity();
		de.setId(id);
		vets_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/vets?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets?search=vetsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property vetsid not found!"));

	} 
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("vetId", "1");

		Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn("vetId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets/1/vetSpecialties?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetVetSpecialties_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("vetId", "1");
		
        Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn("vetId")).thenReturn(joinCol);
		mvc.perform(get("/vets/1/vetSpecialties?search=vetId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
		
	}  
	
	@Test
	public void GetVetSpecialties_searchIsNotEmpty() throws Exception {
	
		Mockito.when(vetsAppService.parseVetSpecialtiesJoinColumn(anyString())).thenReturn(null);
//		mvc.perform(get("/vets/1/vetSpecialties?search=vetid[equals]=1&limit=10&offset=1")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
//		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vets/1/vetSpecialties?search=vetid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid join column"));
	}    
    

}

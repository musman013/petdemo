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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.application.vetspecialties.VetSpecialtiesAppService;
import com.fastcode.demopet.application.vetspecialties.dto.*;
import com.fastcode.demopet.domain.irepository.IVetSpecialtiesRepository;
import com.fastcode.demopet.domain.model.VetSpecialtiesEntity;
import com.fastcode.demopet.domain.irepository.ISpecialtiesRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.SpecialtiesEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.irepository.IVetsRepository;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.application.specialties.SpecialtiesAppService;    
import com.fastcode.demopet.application.vets.VetsAppService;    
import com.fastcode.demopet.domain.model.VetSpecialtiesId;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class VetSpecialtiesControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IVetSpecialtiesRepository vetSpecialties_repository;
	
	@Autowired 
	private ISpecialtiesRepository specialtiesRepository;
	
	@Autowired 
	private IVetsRepository vetsRepository;
	
	@Autowired 
	private IUserRepository userRepository;
	
	@SpyBean
	private VetSpecialtiesAppService vetSpecialtiesAppService;
    
    @SpyBean
	private SpecialtiesAppService specialtiesAppService;
    
    @SpyBean
	private VetsAppService vetsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private VetSpecialtiesEntity vetSpecialties;
	
	private UserEntity user;
	
	private SpecialtiesEntity specialties;

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
		em.createNativeQuery("truncate table sample.vet_specialties").executeUpdate();
		em.createNativeQuery("truncate table sample.specialties").executeUpdate();
		em.createNativeQuery("truncate table sample.f_user").executeUpdate();
		em.createNativeQuery("truncate table sample.vets").executeUpdate();
		em.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
		
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}

//	@Transactional(propagation = Propagation.REQUIRED)
	public VetSpecialtiesEntity createEntity() {
		specialties = createSpecialtiesEntity();
		
		if(!specialtiesRepository.findAll().stream().anyMatch(item -> specialties.getName().equals(item.getName())))
		{
			specialties =specialtiesRepository.save(specialties);
			System.out.println("Spec Id" + specialties.getId());
		}
		VetsEntity vets = createVetsEntity();
		user = createUserEntity();
		vets.setUser(user);
		if(!userRepository.findAll().stream().anyMatch(item -> user.getUserName().equals(item.getUserName()))) {
		
//		if(!userRepository.findAll().contains(user))
//		{
			user = userRepository.save(user);
			System.out.println("User Id" + user.getId());
			vets.setId(user.getId());
			vets.setUser(user);
			vets=vetsRepository.save(vets);
		}
	
		
		VetSpecialtiesEntity vetSpecialties = new VetSpecialtiesEntity();
		vetSpecialties.setSpecialtyId(specialties.getId());
		vetSpecialties.setVetId(vets.getId());
		vetSpecialties.setSpecialties(specialties);
		vetSpecialties.setVets(vets);
		
		
		return vetSpecialties;
	}

//	@Transactional(propagation = Propagation.REQUIRED)
	public CreateVetSpecialtiesInput createVetSpecialtiesInput() {
	
	    CreateVetSpecialtiesInput vetSpecialties = new CreateVetSpecialtiesInput();
		vetSpecialties.setSpecialtyId(2L);
		vetSpecialties.setVetId(2L);
	    
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(2L);
  		specialties.setName("2");
		specialties=specialtiesRepository.save(specialties);
		vetSpecialties.setSpecialtyId(specialties.getId());
		
		VetsEntity vets = new VetsEntity();
		UserEntity user = createUserEntity();
		user.setUserName("u2");
		user = userRepository.save(user);
		vets.setId(user.getId());
		vets.setUser(user);
		vets=vetsRepository.save(vets);
		vetSpecialties.setVetId(vets.getId());
		
		
		return vetSpecialties;
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

	public VetSpecialtiesEntity createNewEntity() {
		VetSpecialtiesEntity vetSpecialties = new VetSpecialtiesEntity();
		vetSpecialties.setSpecialtyId(3L);
		vetSpecialties.setVetId(3L);
		return vetSpecialties;
	}
	
	public SpecialtiesEntity createSpecialtiesEntity() {
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(1L);
  		specialties.setName("1");
		return specialties;
		 
	}
	
	public VetsEntity createVetsEntity() {
		VetsEntity vets = new VetsEntity();
		vets.setId(1L);
		return vets;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final VetSpecialtiesController vetSpecialtiesController = new VetSpecialtiesController(vetSpecialtiesAppService,specialtiesAppService,vetsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(vetSpecialtiesController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		vetSpecialties= createEntity();
		List<VetSpecialtiesEntity> list= vetSpecialties_repository.findAll();
		if(!list.stream().anyMatch(item -> vetSpecialties.getSpecialties().getName().equals(item.getSpecialties().getName()))) {
			vetSpecialties=vetSpecialties_repository.save(vetSpecialties);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId() + ",vetId:" + vetSpecialties.getVetId() )
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

//		mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111")
//				.contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    
	@Test
	public void CreateVetSpecialties_VetSpecialtiesDoesNotExist_ReturnStatusOk() throws Exception {
		
		CreateVetSpecialtiesInput vetSpecialties = createVetSpecialtiesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);

		mvc.perform(post("/vetSpecialties").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     

	@Test
	public void DeleteVetSpecialties_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(vetSpecialtiesAppService).findById(new VetSpecialtiesId(111L, 111L));
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/vetSpecialties/specialtyId:111,vetId:111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a vetSpecialties with a id=specialtyId:111,vetId:111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	    VetSpecialtiesEntity entity =  createNewEntity();
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(3L);
  		specialties.setName("3");
		specialties=specialtiesRepository.save(specialties);
		
		entity.setSpecialtyId(specialties.getId());
		entity.setSpecialties(specialties);
		
		VetsEntity vets = new VetsEntity();
		UserEntity user = createUserEntity();
		user.setUserName("U3");
		user = userRepository.save(user);
		vets.setUser(user);
		vets.setId(user.getId());
		vets=vetsRepository.save(vets);
		
		entity.setVetId(vets.getId());
		entity.setVets(vets);
		
		entity = vetSpecialties_repository.save(entity);

		FindVetSpecialtiesByIdOutput output= new FindVetSpecialtiesByIdOutput();
  		output.setSpecialtyId(entity.getSpecialtyId());
  		output.setVetId(entity.getVetId());
	    Mockito.when(vetSpecialtiesAppService.findById(new VetSpecialtiesId(entity.getSpecialtyId(), entity.getVetId()))).thenReturn(output);
   
		mvc.perform(delete("/vetSpecialties/specialtyId:"+ entity.getSpecialtyId()+ ",vetId:"+ entity.getVetId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateVetSpecialties_VetSpecialtiesDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(vetSpecialtiesAppService).findById(new VetSpecialtiesId(111L, 111L));

		UpdateVetSpecialtiesInput vetSpecialties = new UpdateVetSpecialtiesInput();
		vetSpecialties.setSpecialtyId(111L);
		vetSpecialties.setVetId(111L);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);
//		mvc.perform(put("/vetSpecialties/specialtyId:111,vetId:111").contentType(MediaType.APPLICATION_JSON).content(json))
//		.andExpect(status().isNotFound());
//		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/vetSpecialties/specialtyId:111,vetId:111")
				.contentType(MediaType.APPLICATION_JSON).content(json))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. VetSpecialties with id=specialtyId:111,vetId:111 not found."));

	}    

	@Test
	public void UpdateVetSpecialties_VetSpecialtiesExists_ReturnStatusOk() throws Exception {
		
		VetSpecialtiesEntity entity =  createNewEntity();
		SpecialtiesEntity specialties = new SpecialtiesEntity();
		specialties.setId(5L);
  		specialties.setName("5");
		specialties=specialtiesRepository.save(specialties);
		entity.setSpecialtyId(specialties.getId());
		entity.setSpecialties(specialties);
		
		VetsEntity vets = new VetsEntity();
		vets.setId(5L);
		vets=vetsRepository.save(vets);
		entity.setVetId(vets.getId());
		entity.setVets(vets);
		entity = vetSpecialties_repository.save(entity);
		
		FindVetSpecialtiesByIdOutput output= new FindVetSpecialtiesByIdOutput();
  		output.setSpecialtyId(entity.getSpecialtyId());
  		output.setVetId(entity.getVetId());
	    Mockito.when(vetSpecialtiesAppService.findById(new VetSpecialtiesId(entity.getSpecialtyId(), entity.getVetId()))).thenReturn(output);
        
		UpdateVetSpecialtiesInput vetSpecialties = new UpdateVetSpecialtiesInput();
  		vetSpecialties.setSpecialtyId(entity.getSpecialtyId());
  		vetSpecialties.setVetId(entity.getVetId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(vetSpecialties);
	
		mvc.perform(put("/vetSpecialties/specialtyId:"+ entity.getSpecialtyId()+ ",vetId:"+ entity.getVetId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		VetSpecialtiesEntity de = createEntity();
		de.setSpecialtyId(entity.getSpecialtyId());
		de.setVetId(entity.getVetId());
		vetSpecialties_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/vetSpecialties?search=specialtyId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties?search=vetSpecialtiesspecialtyId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property vetSpecialtiesspecialtyId not found!"));

	} 
	
	@Test
	public void GetSpecialties_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=specialtyId:111"));
	
	}    
	@Test
	public void GetSpecialties_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
//	    mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/specialties")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
	    org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	
	}    
	
	@Test
	public void GetSpecialties_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId()+ ",vetId:" + vetSpecialties.getVetId()+ "/specialties")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	@Test
	public void GetVets_IdIsNotEmptyAndIdIsNotValid_ThrowException() throws Exception {
	
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Invalid id=specialtyId:111"));
	
	}    
	@Test
	public void GetVets_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
//	    mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/vets")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
	    
	    
	    org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/vetSpecialties/specialtyId:111,vetId:111/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	
	}    
	
	@Test
	public void GetVets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/vetSpecialties/specialtyId:" + vetSpecialties.getSpecialtyId()+ ",vetId:" + vetSpecialties.getVetId()+ "/vets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.application.pets.PetsAppService;
import com.fastcode.demopet.application.pets.dto.*;
import com.fastcode.demopet.domain.irepository.IPetsRepository;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.irepository.ITypesRepository;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.irepository.IOwnersRepository;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.application.visits.VisitsAppService;    
import com.fastcode.demopet.application.types.TypesAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.owners.OwnersAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = "spring.profiles.active=test")
public class PetsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IPetsRepository pets_repository;

	@Autowired 
	private ITypesRepository typesRepository;

	@Autowired 
	private IOwnersRepository ownersRepository;

	@Autowired 
	private IUserRepository userRepository;

	@SpyBean
	private PetsAppService petsAppService;

	@SpyBean
	private UserAppService userAppService;

	@SpyBean
	private VisitsAppService visitsAppService;

	@SpyBean
	private TypesAppService typesAppService;

	@SpyBean
	private OwnersAppService ownersAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private PetsEntity pets;

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
		em.createNativeQuery("truncate table sample.pets").executeUpdate();
		em.createNativeQuery("truncate table sample.types").executeUpdate();
		em.createNativeQuery("truncate table sample.owners").executeUpdate();
		em.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
		
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}


	public PetsEntity createEntity() {

		TypesEntity types = createTypesEntity();
		if(!typesRepository.findAll().contains(types))
		{
			types=typesRepository.save(types);
		}

		OwnersEntity owners = createOwnersEntity();
		user = createUserEntity();
		owners.setUser(user);
		if(!userRepository.findAll().stream().anyMatch(item -> user.getUserName().equals(item.getUserName()))) {

			user = userRepository.save(user);
			System.out.println("User Id" + user.getId());
			owners.setId(user.getId());
			owners.setUser(user);
			owners=ownersRepository.save(owners);
		}

		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(1L);
		pets.setName("1");
		pets.setTypes(types);
		pets.setOwners(owners);

		return pets;
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

	public CreatePetsInput createPetsInput() {

		CreatePetsInput pets = new CreatePetsInput();
		pets.setBirthDate(new Date());
		pets.setName("2");


		TypesEntity types = new TypesEntity();
		types.setId(2L);
		types.setName("2");
		types=typesRepository.save(types);
		pets.setTypeId(types.getId());

		OwnersEntity owners = new OwnersEntity();
		owners.setAddress("2");
		owners.setCity("2");
		owners.setId(2L);

		UserEntity user =createUserEntity(); 
		user.setUserName("u2");
		user = userRepository.save(user);
		owners.setId(user.getId());
		owners.setUser(user);
		owners=ownersRepository.save(owners);
		pets.setOwnerId(owners.getId());


		return pets;
	}

	public PetsEntity createNewEntity() {
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(3L);
		pets.setName("3");
		return pets;
	}

	public TypesEntity createTypesEntity() {
		TypesEntity types = new TypesEntity();
		types.setId(1L);
		types.setName("1");
		return types;

	}

	public OwnersEntity createOwnersEntity() {
		OwnersEntity owners = new OwnersEntity();
		owners.setAddress("1");
		owners.setCity("1");
		owners.setId(1L);
		return owners;

	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final PetsController petsController = new PetsController(petsAppService, visitsAppService, userAppService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(petsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		pets= createEntity();
		List<PetsEntity> list= pets_repository.findAll();
		if(!list.stream().anyMatch(item -> pets.getOwners().getUser().getUserName().equals(item.getOwners().getUser().getUserName()))) {

			pets=pets_repository.save(pets);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/pets/" + pets.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		//		mvc.perform(get("/pets/111")
		//				.contentType(MediaType.APPLICATION_JSON))
		//		.andExpect(status().isNotFound());

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    

	@Test
	public void CreatePets_PetsDoesNotExist_ReturnStatusOk() throws Exception {


		TypesEntity types = createTypesEntity();
		typesRepository.save(types);
		OwnersEntity owners = createOwnersEntity();
		UserEntity user = createUserEntity(); 
		user.setUserName("umk8");
		user = userRepository.save(user);
		owners.setId(user.getId());
		owners.setUser(user);
		ownersRepository.save(owners);
		CreatePetsInput pets = createPetsInput();
		pets.setTypeId(types.getId());
		pets.setOwnerId(user.getId());
		pets.setName("abc");
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);

		//	UserEntity user = Mockito.mock(UserEntity.class);
		doReturn(user).when(userAppService).getUser();
		mvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     
	@Test
	public void CreatePets_typesDoesNotExists_ThrowEntityNotFoundException() throws Exception {

		UserEntity user = createUserEntity();
		CreatePetsInput pets = new CreatePetsInput();
		pets.setName("p1");

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);

		//		org.assertj.core.api.Assertions.assertThatThrownBy(() ->
		//		mvc.perform(post("/pets")
		//				.contentType(MediaType.APPLICATION_JSON).content(json))
		//		.andExpect(status().isNotFound()));
		doReturn(user).when(userAppService).getUser();
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(post("/pets")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("No record found"));
	}

	//	@Test
	//	public void CreatePets_ownersDoesNotExists_ThrowEntityNotFoundException() throws Exception {
	//
	//		CreatePetsInput pets = createPetsInput();
	//		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	//		String json = ow.writeValueAsString(pets);
	//
	////		org.assertj.core.api.Assertions.assertThatThrownBy(() ->
	////		mvc.perform(post("/pets")
	////				.contentType(MediaType.APPLICATION_JSON).content(json))
	////		.andExpect(status().isNotFound()));
	//		
	//		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(post("/pets")
	//				.contentType(MediaType.APPLICATION_JSON))
	//				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	//
	//	}    

	@Test
	public void DeletePets_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

		doReturn(null).when(petsAppService).findById(111L);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/pets/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a pets with a id=111"));
	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {

		PetsEntity entity =  createNewEntity();
		TypesEntity types = new TypesEntity();
		types.setId(3L);
		types.setName("3");
		types=typesRepository.save(types);

		entity.setTypes(types);
		OwnersEntity owners = new OwnersEntity();
		owners.setAddress("3");
		owners.setCity("3");
		//	owners.setId(3L);

		UserEntity user =createUserEntity(); 
		user.setUserName("u4");
		user = userRepository.save(user);
		owners.setId(user.getId());
		owners.setUser(user);
		owners=ownersRepository.save(owners);

		entity.setOwners(owners);

		entity = pets_repository.save(entity);

		FindPetsByIdOutput output= new FindPetsByIdOutput();
		output.setId(entity.getId());
		Mockito.when(petsAppService.findById(entity.getId())).thenReturn(output);

		mvc.perform(delete("/pets/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  

	@Test
	public void UpdatePets_PetsDoesNotExist_ReturnStatusNotFound() throws Exception {

		doReturn(null).when(petsAppService).findById(111L);

		UpdatePetsInput pets = new UpdatePetsInput();
		pets.setBirthDate(new Date());
		pets.setId(111L);
		pets.setName("111");

		//	UserEntity user =createUserEntity(); 
		//	user.setUserName("u4");


		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);

		//		mvc.perform(put("/pets/111").contentType(MediaType.APPLICATION_JSON).content(json))
		//		.andExpect(status().isNotFound());

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/pets/111")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Pets with id=111 not found."));
	}    

	@Test
	public void UpdatePets_PetsExists_ReturnStatusOk() throws Exception {
		PetsEntity entity =  createNewEntity();
		TypesEntity types = new TypesEntity();
		types.setId(5L);
		types.setName("5");
		types=typesRepository.save(types);
		entity.setTypes(types);
		OwnersEntity owners = new OwnersEntity();
		owners.setAddress("5");
		owners.setCity("5");
		owners.setId(5L);
		owners=ownersRepository.save(owners);
		entity.setOwners(owners);
		entity = pets_repository.save(entity);
		FindPetsByIdOutput output= new FindPetsByIdOutput();
		output.setBirthDate(entity.getBirthDate());
		output.setId(entity.getId());
		output.setName(entity.getName());
		Mockito.when(petsAppService.findById(entity.getId())).thenReturn(output);

		UpdatePetsInput pets = new UpdatePetsInput();
		pets.setId(entity.getId());

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(pets);

		mvc.perform(put("/pets/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		PetsEntity de = createEntity();
		de.setId(entity.getId());
		pets_repository.delete(de);

	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {
		UserEntity user = Mockito.mock(UserEntity.class);
		doReturn(user).when(userAppService).getUser();
		doReturn(true).when(userAppService).checkIsAdmin(any(UserEntity.class));

		mvc.perform(get("/pets?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets?search=petsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property petsid not found!"));

	} 

	@Test
	public void GetVisits_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("petId", "1");

		Mockito.when(petsAppService.parseVisitsJoinColumn("petId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/1/visits?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));

	}    

	@Test
	public void GetVisits_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("petId", "1");

		Mockito.when(petsAppService.parseVisitsJoinColumn("petId")).thenReturn(joinCol);
		mvc.perform(get("/pets/1/visits?search=petId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());

	}  

	@Test
	public void GetVisits_searchIsNotEmpty() throws Exception {

		Mockito.when(petsAppService.parseVisitsJoinColumn(anyString())).thenReturn(null);
		//		mvc.perform(get("/pets/1/visits?search=petid[equals]=1&limit=10&offset=1")
		//				.contentType(MediaType.APPLICATION_JSON))
		//	    		  .andExpect(status().isNotFound());
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/111/owners")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	}    

	@Test
	public void GetTypes_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {

		//	    mvc.perform(get("/pets/111/types")
		//				.contentType(MediaType.APPLICATION_JSON))
		//	    		  .andExpect(status().isNotFound());
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/111/types")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    

	@Test
	public void GetTypes_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		mvc.perform(get("/pets/" + pets.getId()+ "/types")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void GetOwners_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {

		//	    mvc.perform(get("/pets/111/owners")
		//				.contentType(MediaType.APPLICATION_JSON))
		//	    		  .andExpect(status().isNotFound());

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/pets/111/owners")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    

	@Test
	public void GetOwners_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {

		mvc.perform(get("/pets/" + pets.getId()+ "/owners")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  


}

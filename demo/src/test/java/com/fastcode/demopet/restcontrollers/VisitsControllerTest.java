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
import com.fastcode.demopet.application.visits.VisitsAppService;
import com.fastcode.demopet.application.visits.dto.*;
import com.fastcode.demopet.domain.irepository.IVisitsRepository;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.irepository.IPetsRepository;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.application.invoices.InvoicesAppService;    
import com.fastcode.demopet.application.pets.PetsAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class VisitsControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IVisitsRepository visits_repository;
	
	@Autowired 
	private IPetsRepository petsRepository;
	
	@SpyBean
	private VisitsAppService visitsAppService;
    
    @SpyBean
	private InvoicesAppService invoicesAppService;
    
    @SpyBean
	private PetsAppService petsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private VisitsEntity visits;

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
		em.createNativeQuery("truncate table sample.visits").executeUpdate();
		em.createNativeQuery("truncate table sample.pets").executeUpdate();
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}


	public VisitsEntity createEntity() {
		PetsEntity pets = createPetsEntity();
		if(!petsRepository.findAll().contains(pets))
		{
			pets=petsRepository.save(pets);
		}
	
		VisitsEntity visits = new VisitsEntity();
  		visits.setDescription("1");
		visits.setId(1L);
		visits.setVisitDate(new Date());
		visits.setPets(pets);
		
		return visits;
	}

	public CreateVisitsInput createVisitsInput() {
	
	    CreateVisitsInput visits = new CreateVisitsInput();
  		visits.setDescription("2");
		visits.setVisitDate(new Date());
	    
		
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(2L);
  		pets.setName("2");
		pets=petsRepository.save(pets);
		visits.setPetId(pets.getId());
		
		
		return visits;
	}

	public VisitsEntity createNewEntity() {
		VisitsEntity visits = new VisitsEntity();
  		visits.setDescription("3");
		visits.setId(3L);
		visits.setVisitDate(new Date());
		return visits;
	}
	
	public PetsEntity createPetsEntity() {
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(1L);
  		pets.setName("1");
		return pets;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final VisitsController visitsController = new VisitsController(visitsAppService,invoicesAppService,petsAppService,
	logHelper);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(visitsController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		visits= createEntity();
		List<VisitsEntity> list= visits_repository.findAll();
		if(!list.contains(visits)) {
			visits=visits_repository.save(visits);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/visits/" + visits.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

		mvc.perform(get("/visits/111")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

	}    
	@Test
	public void CreateVisits_VisitsDoesNotExist_ReturnStatusOk() throws Exception {
		PetsEntity pets = createPetsEntity();
		petsRepository.save(pets);
		CreateVisitsInput visits = createVisitsInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(visits);

		mvc.perform(post("/visits").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     
	@Test
	public void CreateVisits_petsDoesNotExists_ThrowEntityNotFoundException() throws Exception {

		CreateVisitsInput visits = createVisitsInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(visits);

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->
		mvc.perform(post("/visits")
				.contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound()));

	}    

	@Test
	public void DeleteVisits_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(visitsAppService).findById(111L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/visits/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a visits with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	 VisitsEntity entity =  createNewEntity();
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(3L);
  		pets.setName("3");
		pets=petsRepository.save(pets);
		
		entity.setPets(pets);
		
		entity = visits_repository.save(entity);

		FindVisitsByIdOutput output= new FindVisitsByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(visitsAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/visits/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateVisits_VisitsDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(visitsAppService).findById(111L);

		UpdateVisitsInput visits = new UpdateVisitsInput();
  		visits.setDescription("111");
		visits.setId(111L);
		visits.setVisitDate(new Date());

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(visits);
		mvc.perform(put("/visits/111").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound());

	}    

	@Test
	public void UpdateVisits_VisitsExists_ReturnStatusOk() throws Exception {
		VisitsEntity entity =  createNewEntity();
		PetsEntity pets = new PetsEntity();
		pets.setBirthDate(new Date());
		pets.setId(5L);
  		pets.setName("5");
		pets=petsRepository.save(pets);
		entity.setPets(pets);
		entity = visits_repository.save(entity);
		FindVisitsByIdOutput output= new FindVisitsByIdOutput();
  		output.setDescription(entity.getDescription());
  		output.setId(entity.getId());
  		output.setVisitDate(entity.getVisitDate());
        Mockito.when(visitsAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateVisitsInput visits = new UpdateVisitsInput();
  		visits.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(visits);
	
		mvc.perform(put("/visits/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		VisitsEntity de = createEntity();
		de.setId(entity.getId());
		visits_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		mvc.perform(get("/visits?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/visits?search=visitsid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property visitsid not found!"));

	} 
	
	@Test
	public void GetInvoices_searchIsNotEmptyAndPropertyIsNotValid_ThrowException() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("visitId", "1");

		Mockito.when(visitsAppService.parseInvoicesJoinColumn("visitId")).thenReturn(joinCol);
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/visits/1/invoices?search=abc[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property abc not found!"));
	
	}    
	
	@Test
	public void GetInvoices_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
		Map<String,String> joinCol = new HashMap<String,String>();
		joinCol.put("visitId", "1");
		
        Mockito.when(visitsAppService.parseInvoicesJoinColumn("visitId")).thenReturn(joinCol);
		mvc.perform(get("/visits/1/invoices?search=visitId[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
	
	@Test
	public void GetInvoices_searchIsNotEmpty() throws Exception {
	
		Mockito.when(visitsAppService.parseInvoicesJoinColumn(anyString())).thenReturn(null);
		mvc.perform(get("/visits/1/invoices?search=visitid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	}    
	@Test
	public void GetPets_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
	
	    mvc.perform(get("/visits/111/pets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isNotFound());
	
	}    
	
	@Test
	public void GetPets_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/visits/" + visits.getId()+ "/pets")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}

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

import com.fastcode.demopet.application.visits.dto.Status;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.invoices.InvoicesAppService;
import com.fastcode.demopet.application.invoices.dto.*;
import com.fastcode.demopet.domain.irepository.IInvoicesRepository;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.irepository.IVisitsRepository;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.application.visits.VisitsAppService;    

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
				properties = "spring.profiles.active=test")
public class InvoicesControllerTest {
	@Autowired
	private SortHandlerMethodArgumentResolver sortArgumentResolver;

	@Autowired 
	private IInvoicesRepository invoices_repository;
	
	@Autowired 
	private IVisitsRepository visitsRepository;
	
	@SpyBean
	private InvoicesAppService invoicesAppService;
	
	@SpyBean
	private UserAppService userAppService;
    
    @SpyBean
	private VisitsAppService visitsAppService;

	@SpyBean
	private LoggingHelper logHelper;

	@Mock
	private Logger loggerMock;

	private InvoicesEntity invoices;

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
		em.createNativeQuery("truncate table sample.invoices").executeUpdate();
		em.createNativeQuery("truncate table sample.visits").executeUpdate();
		em.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
		
		em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
		em.getTransaction().commit();
	}


	public InvoicesEntity createEntity() {
		VisitsEntity visits = createVisitsEntity();
		if(!visitsRepository.findAll().contains(visits))
		{
			visits=visitsRepository.save(visits);
		}
	
		InvoicesEntity invoices = new InvoicesEntity();
		invoices.setAmount(1L);
		invoices.setId(1L);
		invoices.setStatus(InvoiceStatus.Unpaid);
		invoices.setVisits(visits);
		
		return invoices;
	}

	public CreateInvoicesInput createInvoicesInput() {
	
	    CreateInvoicesInput invoices = new CreateInvoicesInput();
		invoices.setAmount(2L);
		invoices.setStatus(InvoiceStatus.Unpaid);
	    
		VisitsEntity visits = new VisitsEntity();
  		visits.setDescription("2");
		visits.setId(2L);
		visits.setStatus(Status.CREATED);
		visits.setVisitDate(new Date());
		visits=visitsRepository.save(visits);
		invoices.setVisitId(visits.getId());
		
		return invoices;
	}

	public InvoicesEntity createNewEntity() {
		InvoicesEntity invoices = new InvoicesEntity();
		invoices.setAmount(3L);
		invoices.setStatus(InvoiceStatus.Unpaid);
		invoices.setId(3L);
		return invoices;
	}
	
	public VisitsEntity createVisitsEntity() {
		VisitsEntity visits = new VisitsEntity();
		visits.setStatus(Status.CREATED);
  		visits.setDescription("1");
		visits.setId(1L);
		visits.setVisitDate(new Date());
		return visits;
		 
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final InvoicesController invoicesController = new InvoicesController(invoicesAppService, userAppService);
	//	when(logHelper.getLogger()).thenReturn(loggerMock);
	//
		doNothing().when(loggerMock).error(anyString());

		this.mvc = MockMvcBuilders.standaloneSetup(invoicesController)
				.setCustomArgumentResolvers(sortArgumentResolver)
				.setControllerAdvice()
				.build();
	}

	@Before
	public void initTest() {

		invoices= createEntity();
		List<InvoicesEntity> list= invoices_repository.findAll();
		if(!list.contains(invoices)) {
			invoices=invoices_repository.save(invoices);
		}

	}

	@Test
	public void FindById_IdIsValid_ReturnStatusOk() throws Exception {
	
		mvc.perform(get("/invoices/" + invoices.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}  

	@Test
	public void FindById_IdIsNotValid_ReturnStatusNotFound() throws Exception {

//		mvc.perform(get("/invoices/111")
//				.contentType(MediaType.APPLICATION_JSON))
//		.andExpect(status().isNotFound());
		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/invoices/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));

	}    
	@Test
	public void CreateInvoices_InvoicesDoesNotExist_ReturnStatusOk() throws Exception {
		VisitsEntity visits = createVisitsEntity();
		visitsRepository.save(visits);
		CreateInvoicesInput invoices = createInvoicesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(invoices);

		mvc.perform(post("/invoices").contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

	}     
	@Test
	public void CreateInvoices_visitsDoesNotExists_ThrowEntityNotFoundException() throws Exception {

		CreateInvoicesInput invoices = createInvoicesInput();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(invoices);

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->
		mvc.perform(post("/invoices")
				.contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isNotFound()));

	}    

	@Test
	public void DeleteInvoices_IdIsNotValid_ThrowEntityNotFoundException() throws Exception {

        doReturn(null).when(invoicesAppService).findById(111L);
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(delete("/invoices/111")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("There does not exist a invoices with a id=111"));

	}  

	@Test
	public void Delete_IdIsValid_ReturnStatusNoContent() throws Exception {
	
	    InvoicesEntity entity =  createNewEntity();
		VisitsEntity visits = new VisitsEntity();
  		visits.setDescription("3");
		visits.setId(3L);
		visits.setStatus(Status.CONFIRMED);
		visits.setVisitDate(new Date());
		visits=visitsRepository.save(visits);
		
		entity.setVisits(visits);
		
		entity = invoices_repository.save(entity);

		FindInvoicesByIdOutput output= new FindInvoicesByIdOutput();
  		output.setId(entity.getId());
        Mockito.when(invoicesAppService.findById(entity.getId())).thenReturn(output);
        
		mvc.perform(delete("/invoices/" + entity.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
	}  


	@Test
	public void UpdateInvoices_InvoicesDoesNotExist_ReturnStatusNotFound() throws Exception {

        doReturn(null).when(invoicesAppService).findById(111L);

		UpdateInvoicesInput invoices = new UpdateInvoicesInput();
		invoices.setAmount(111L);
		invoices.setId(111L);
		invoices.setStatus(InvoiceStatus.Paid);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(invoices);
//		mvc.perform(put("/invoices/111").contentType(MediaType.APPLICATION_JSON).content(json))
//		.andExpect(status().isNotFound());
		
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(put("/invoices/111")
				.contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Unable to update. Invoices with id=111 not found."));


	}    

	@Test
	public void UpdateInvoices_InvoicesExists_ReturnStatusOk() throws Exception {
		InvoicesEntity entity =  createNewEntity();
		VisitsEntity visits = new VisitsEntity();
  		visits.setDescription("5");
		visits.setId(5L);
		visits.setStatus(Status.CREATED);
		visits.setVisitDate(new Date());
		visits=visitsRepository.save(visits);
		entity.setVisits(visits);
		entity = invoices_repository.save(entity);
		FindInvoicesByIdOutput output= new FindInvoicesByIdOutput();
  		output.setAmount(entity.getAmount());
  		output.setId(entity.getId());
        Mockito.when(invoicesAppService.findById(entity.getId())).thenReturn(output);
        
		UpdateInvoicesInput invoices = new UpdateInvoicesInput();
  		invoices.setId(entity.getId());
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(invoices);
	
		mvc.perform(put("/invoices/" + entity.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
		.andExpect(status().isOk());

		InvoicesEntity de = createEntity();
		de.setId(entity.getId());
		invoices_repository.delete(de);

	}    
	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsValid_ReturnStatusOk() throws Exception {

		 UserEntity user = Mockito.mock(UserEntity.class);
		 doReturn(user).when(userAppService).getUser();
		 doReturn(true).when(userAppService).checkIsAdmin(any(UserEntity.class));

		mvc.perform(get("/invoices?search=id[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}    

	@Test
	public void FindAll_SearchIsNotNullAndPropertyIsNotValid_ThrowException() throws Exception {

//		UserEntity user = Mockito.mock(UserEntity.class);
//		 doReturn(user).when(userAppService).getUser();
//		 doReturn(true).when(userAppService).checkIsAdmin(any(UserEntity.class));

		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/invoices?search=invoicesid[equals]=1&limit=10&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new Exception("Wrong URL Format: Property invoicesid not found!"));

	} 
	
	@Test
	public void GetVisits_IdIsNotEmptyAndIdDoesNotExist_ReturnNotFound() throws Exception {
//	
//	    mvc.perform(get("/invoices/111/visits")
//				.contentType(MediaType.APPLICATION_JSON))
//	    		  .andExpect(status().isNotFound());
//	    
		org.assertj.core.api.Assertions.assertThatThrownBy(() ->  mvc.perform(get("/invoices/111/visits")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())).hasCause(new EntityNotFoundException("Not found"));
	
	}    
	
	@Test
	public void GetVisits_searchIsNotEmptyAndPropertyIsValid_ReturnList() throws Exception {
	
	   mvc.perform(get("/invoices/" + invoices.getId()+ "/visits")
				.contentType(MediaType.APPLICATION_JSON))
	    		  .andExpect(status().isOk());
	}  
    

}

package com.fastcode.demopet.application.invoices;

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

import com.fastcode.demopet.domain.invoices.*;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.application.invoices.dto.*;
import com.fastcode.demopet.domain.model.QInvoicesEntity;
import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.visits.VisitsManager;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class InvoicesAppServiceTest {

	@InjectMocks
	@Spy
	InvoicesAppService _appService;

	@Mock
	private InvoicesManager _invoicesManager;
	
    @Mock
	private VisitsManager  _visitsManager;
	
	@Mock
	private InvoicesMapper _mapper;

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
	public void findInvoicesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_invoicesManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findInvoicesById_IdIsNotNullAndIdExists_ReturnInvoices() {

		InvoicesEntity invoices = mock(InvoicesEntity.class);
		Mockito.when(_invoicesManager.findById(anyLong())).thenReturn(invoices);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.invoicesEntityToFindInvoicesByIdOutput(invoices));
	}
	
	 @Test 
    public void createInvoices_InvoicesIsNotNullAndInvoicesDoesNotExist_StoreInvoices() { 
 
       InvoicesEntity invoicesEntity = mock(InvoicesEntity.class); 
       CreateInvoicesInput invoices = new CreateInvoicesInput();
   
		VisitsEntity visits= mock(VisitsEntity.class);
        invoices.setVisitId(Long.valueOf(ID));
		Mockito.when(_visitsManager.findById(
        any(Long.class))).thenReturn(visits);
		
        Mockito.when(_mapper.createInvoicesInputToInvoicesEntity(any(CreateInvoicesInput.class))).thenReturn(invoicesEntity); 
        Mockito.when(_invoicesManager.create(any(InvoicesEntity.class))).thenReturn(invoicesEntity);
      
        Assertions.assertThat(_appService.create(invoices)).isEqualTo(_mapper.invoicesEntityToCreateInvoicesOutput(invoicesEntity)); 
    } 
	@Test
	public void createInvoices_InvoicesIsNotNullAndInvoicesDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		CreateInvoicesInput invoices = mock(CreateInvoicesInput.class);
		
		Mockito.when(_mapper.createInvoicesInputToInvoicesEntity(any(CreateInvoicesInput.class))).thenReturn(null); 
		Assertions.assertThat(_appService.create(invoices)).isEqualTo(null);
	}
	
	@Test
	public void createInvoices_InvoicesIsNotNullAndInvoicesDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {

		CreateInvoicesInput invoices = new CreateInvoicesInput();
	    
        invoices.setVisitId(Long.valueOf(ID));
     
		Mockito.when(_visitsManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.create(invoices)).isEqualTo(null);
    }
    
    @Test
	public void updateInvoices_InvoicesIsNotNullAndInvoicesDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		UpdateInvoicesInput invoices = mock(UpdateInvoicesInput.class);
		InvoicesEntity invoicesEntity = mock(InvoicesEntity.class); 
		
		Mockito.when(_mapper.updateInvoicesInputToInvoicesEntity(any(UpdateInvoicesInput.class))).thenReturn(invoicesEntity); 
		Assertions.assertThat(_appService.update(ID,invoices)).isEqualTo(null);
	}
	
	@Test
	public void updateInvoices_InvoicesIsNotNullAndInvoicesDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {
		
		UpdateInvoicesInput invoices = new UpdateInvoicesInput();
        invoices.setVisitId(Long.valueOf(ID));
		
     
		Mockito.when(_visitsManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.update(ID,invoices)).isEqualTo(null);
	}

		
	@Test
	public void updateInvoices_InvoicesIdIsNotNullAndIdExists_ReturnUpdatedInvoices() {

		InvoicesEntity invoicesEntity = mock(InvoicesEntity.class);
		UpdateInvoicesInput invoices= mock(UpdateInvoicesInput.class);
		
		Mockito.when(_mapper.updateInvoicesInputToInvoicesEntity(any(UpdateInvoicesInput.class))).thenReturn(invoicesEntity);
		Mockito.when(_invoicesManager.update(any(InvoicesEntity.class))).thenReturn(invoicesEntity);
		Assertions.assertThat(_appService.update(ID,invoices)).isEqualTo(_mapper.invoicesEntityToUpdateInvoicesOutput(invoicesEntity));
	}
    
	@Test
	public void deleteInvoices_InvoicesIsNotNullAndInvoicesExists_InvoicesRemoved() {

		InvoicesEntity invoices= mock(InvoicesEntity.class);
		Mockito.when(_invoicesManager.findById(anyLong())).thenReturn(invoices);
		
		_appService.delete(ID); 
		verify(_invoicesManager).delete(invoices);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<InvoicesEntity> list = new ArrayList<>();
		Page<InvoicesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindInvoicesByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_invoicesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<InvoicesEntity> list = new ArrayList<>();
		InvoicesEntity invoices = mock(InvoicesEntity.class);
		list.add(invoices);
    	Page<InvoicesEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindInvoicesByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
//		search.setType(1);
//		search.setValue("xyz");
//		search.setOperator("equals");
		output.add(_mapper.invoicesEntityToFindInvoicesByIdOutput(invoices));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_invoicesManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QInvoicesEntity invoices = QInvoicesEntity.invoicesEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(invoices,map,searchMap)).isEqualTo(builder);
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
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QInvoicesEntity invoices = QInvoicesEntity.invoicesEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QInvoicesEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Visits
	@Test
	public void GetVisits_IfInvoicesIdAndVisitsIdIsNotNullAndInvoicesExists_ReturnVisits() {
		InvoicesEntity invoices = mock(InvoicesEntity.class);
		VisitsEntity visits = mock(VisitsEntity.class);

		Mockito.when(_invoicesManager.findById(anyLong())).thenReturn(invoices);
		Mockito.when(_invoicesManager.getVisits(anyLong())).thenReturn(visits);
		Assertions.assertThat(_appService.getVisits(ID)).isEqualTo(_mapper.visitsEntityToGetVisitsOutput(visits, invoices));
	}

	@Test 
	public void GetVisits_IfInvoicesIdAndVisitsIdIsNotNullAndInvoicesDoesNotExist_ReturnNull() {
		Mockito.when(_invoicesManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.getVisits(ID)).isEqualTo(null);
	}
	
}


package com.fastcode.demopet.application.visits;

import static org.mockito.Mockito.*;

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

import com.fastcode.demopet.domain.visits.*;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.application.pets.dto.FindPetsByIdOutput;
import com.fastcode.demopet.application.visits.dto.*;
import com.fastcode.demopet.domain.model.QVisitsEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.owners.OwnersManager;
import com.fastcode.demopet.domain.invoices.InvoicesManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.pets.PetsManager;
import com.fastcode.demopet.domain.vets.VetsManager;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitsAppServiceTest {

	@InjectMocks
	@Spy
	VisitsAppService _appService;

	@Mock
	private VisitsManager _visitsManager;
	
	@Mock
	private OwnersManager _ownersManager;
	
	@Mock
	private VetsManager _vetsManager;
	
    @Mock
	private InvoicesManager  _invoicesManager;
    
    @Mock
   	private VisitMailUtils _visitMailUtils;
	
    @Mock
	private PetsManager  _petsManager;
	
	@Mock
	private IVisitsMapper _mapper;

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
	public void findVisitsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findVisitsById_IdIsNotNullAndIdExists_ReturnVisits() {

		VisitsEntity visits = mock(VisitsEntity.class);
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
	}
	
	 @Test 
    public void createVisits_VisitsIsNotNullAndVisitsDoesNotExist_StoreVisits() { 
 
       VisitsEntity visitsEntity = new VisitsEntity(); 
       CreateVisitsInput visits = new CreateVisitsInput();
       
		PetsEntity pets= mock(PetsEntity.class);
        visits.setPetId(Long.valueOf(ID));
		Mockito.when(_petsManager.findById(
        any(Long.class))).thenReturn(pets);
		
        Mockito.when(_mapper.createVisitsInputToVisitsEntity(any(CreateVisitsInput.class))).thenReturn(visitsEntity); 
        Mockito.when(_visitsManager.create(any(VisitsEntity.class))).thenReturn(visitsEntity);
        doNothing().when(_visitMailUtils).buildVisitConfirmationMail(any(Long.class));
        doNothing().when(_visitMailUtils).scheduleReminderJob(any(Long.class));
        Assertions.assertThat(_appService.create(visits)).isEqualTo(_mapper.visitsEntityToCreateVisitsOutput(visitsEntity)); 
    } 
	@Test
	public void createVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		CreateVisitsInput visits = mock(CreateVisitsInput.class);
		VisitsEntity visitsEntity = new VisitsEntity(); 
		 
		Mockito.when(_mapper.createVisitsInputToVisitsEntity(any(CreateVisitsInput.class))).thenReturn(visitsEntity); 
		Assertions.assertThat(_appService.create(visits)).isEqualTo(null);
	}
	
	@Test
	public void createVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {

		CreateVisitsInput visits = new CreateVisitsInput();
		VisitsEntity visitsEntity = new VisitsEntity(); 
        visits.setPetId(Long.valueOf(ID));
     
        Mockito.when(_mapper.createVisitsInputToVisitsEntity(any(CreateVisitsInput.class))).thenReturn(visitsEntity); 
		
		Mockito.when(_petsManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.create(visits)).isEqualTo(null);
    }
    
    @Test
	public void updateVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		UpdateVisitsInput visits = mock(UpdateVisitsInput.class);
		VisitsEntity visitsEntity = mock(VisitsEntity.class); 
		
		Mockito.when(_mapper.updateVisitsInputToVisitsEntity(any(UpdateVisitsInput.class))).thenReturn(visitsEntity); 
		Assertions.assertThat(_appService.update(ID,visits)).isEqualTo(null);
	}
	
	@Test
	public void updateVisits_VisitsIsNotNullAndVisitsDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {
		
		UpdateVisitsInput visits = new UpdateVisitsInput();
        visits.setPetId(Long.valueOf(ID));
     
		Mockito.when(_petsManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.update(ID,visits)).isEqualTo(null);
	}
		
	@Test
	public void updateVisits_VisitsIdIsNotNullAndIdExists_ReturnUpdatedVisits() {

		VisitsEntity visitsEntity = mock(VisitsEntity.class);
		UpdateVisitsInput visits= mock(UpdateVisitsInput.class);
		
		Mockito.when(_mapper.updateVisitsInputToVisitsEntity(any(UpdateVisitsInput.class))).thenReturn(visitsEntity);
		Mockito.when(_visitsManager.update(any(VisitsEntity.class))).thenReturn(visitsEntity);
		Assertions.assertThat(_appService.update(ID,visits)).isEqualTo(_mapper.visitsEntityToUpdateVisitsOutput(visitsEntity));
	}
    
	@Test
	public void deleteVisits_VisitsIsNotNullAndVisitsExists_VisitsRemoved() {

		VisitsEntity visits= new VisitsEntity();
		PetsEntity pets = mock(PetsEntity.class);
		visits.setPets(pets);
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
		
		_appService.delete(ID); 
		verify(_visitsManager).delete(visits);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<VisitsEntity> list = new ArrayList<>();
		Page<VisitsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVisitsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		doReturn(new BooleanBuilder()).when(_appService).search(any(SearchCriteria.class));
		Mockito.when(_visitsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<VisitsEntity> list = new ArrayList<>();
		VisitsEntity visits = mock(VisitsEntity.class);
		list.add(visits);
    	Page<VisitsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindVisitsByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
		output.add(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_visitsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}

	@Test
	public void changeStatus_VisitExistsAndVisitNotesAreNull_ReturnUpdatedVisitObject() {
		
		VisitsEntity visits = new VisitsEntity();
		FindVisitsByIdOutput  output = new FindVisitsByIdOutput();
		
		UpdateVisitStatus input = new UpdateVisitStatus();
		input.setStatus(Status.CONFIRMED);
		
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
	    Mockito.doReturn(visits).when(_visitsManager).update(any(VisitsEntity.class));
	    Mockito.when(_mapper.visitsEntityToFindVisitsByIdOutput(any(VisitsEntity.class))).thenReturn(output);
	    Assertions.assertThat(_appService.changeStatus(2L, input)).isEqualTo(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
	}
	
	@Test
	public void changeStatus_VisitExistsAndVisitNotesAreNotNull_ReturnUpdatedVisitObject() {
		
		VisitsEntity visits = new VisitsEntity();
		FindVisitsByIdOutput  output = new FindVisitsByIdOutput();
		
		UpdateVisitStatus input = new UpdateVisitStatus();
		input.setStatus(Status.CONFIRMED);
		input.setVisitNotes("abc");
		
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
	    Mockito.doReturn(visits).when(_visitsManager).update(any(VisitsEntity.class));
	    Mockito.when(_mapper.visitsEntityToFindVisitsByIdOutput(any(VisitsEntity.class))).thenReturn(output);
	    Assertions.assertThat(_appService.changeStatus(2L, input)).isEqualTo(_mapper.visitsEntityToFindVisitsByIdOutput(visits));
	}
	
	@Test
	public void filterVisits_ListIsNotEmptyAndVisitExistsAgainstOwner_ReturnList() {
		
		List<FindVisitsByIdOutput> list = new ArrayList<>();
		FindVisitsByIdOutput visits = new FindVisitsByIdOutput();
		
		OwnersEntity owners = new OwnersEntity();
		owners.setId(2L);
		PetsEntity pets = new PetsEntity();
		pets.setOwners(owners);
		visits.setPetId(2L);
		
		list.add(visits);
		
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(owners);
		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(null);
		Mockito.when(_petsManager.findById(anyLong())).thenReturn(pets);
		Assertions.assertThat(_appService.filterVisits(list, 2L)).isEqualTo(list);
	}
	
	@Test
	public void filterVisits_ListIsNotEmptyAndVisitExistsAgainstVet_ReturnList() {
		
		List<FindVisitsByIdOutput> list = new ArrayList<>();
		FindVisitsByIdOutput visits = new FindVisitsByIdOutput();
		
		VetsEntity vets = new VetsEntity();
		vets.setId(2L);

		visits.setVetId(2L);
		
		list.add(visits);
		
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(null);
		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(vets);
	
		Assertions.assertThat(_appService.filterVisits(list, 2L)).isEqualTo(list);
	}
	
	@Test
	public void filterVisits_ListIsNotEmptyAndPetDoesNotExistsAgainstOwner_ReturnEmptyList() {
		
		List<FindVisitsByIdOutput> list = new ArrayList<>();
		FindVisitsByIdOutput visits = new FindVisitsByIdOutput();
		
		list.add(visits);
		
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(null);
		Mockito.when(_vetsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.filterVisits(list, 2L)).isEqualTo(new ArrayList<>());
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QVisitsEntity visits = QVisitsEntity.visitsEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("description",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(visits.description.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(visits,map,searchMap)).isEqualTo(builder);
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
        list.add("description");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QVisitsEntity visits = QVisitsEntity.visitsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("description");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(visits.description.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QVisitsEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Pets
	@Test
	public void GetPets_IfVisitsIdAndPetsIdIsNotNullAndVisitsExists_ReturnPets() {
		VisitsEntity visits = mock(VisitsEntity.class);
		PetsEntity pets = mock(PetsEntity.class);

		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
		Mockito.when(_visitsManager.getPets(anyLong())).thenReturn(pets);
		Assertions.assertThat(_appService.getPets(ID)).isEqualTo(_mapper.petsEntityToGetPetsOutput(pets, visits));
	}

	@Test 
	public void GetPets_IfVisitsIdAndPetsIdIsNotNullAndVisitsDoesNotExist_ReturnNull() {
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.getPets(ID)).isEqualTo(null);
	}
	
	@Test
	public void ParseInvoicesJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("visitId", keyString);
		Assertions.assertThat(_appService.parseInvoicesJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}


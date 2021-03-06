package com.fastcode.demopet.application.pets;

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

import com.fastcode.demopet.domain.pets.*;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.application.pets.dto.*;
import com.fastcode.demopet.domain.model.QPetsEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.visits.VisitsManager;
import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.domain.types.TypesManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.owners.OwnersManager;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class PetsAppServiceTest {

	@InjectMocks
	@Spy
	PetsAppService _appService;

	@Mock
	private PetsManager _petsManager;
	
    @Mock
	private VisitsManager  _visitsManager;
	
    @Mock
	private TypesManager  _typesManager;
	
    @Mock
	private OwnersManager  _ownersManager;
	
	@Mock
	private IPetsMapper _mapper;

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
	public void findPetsById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_petsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findPetsById_IdIsNotNullAndIdExists_ReturnPets() {

		PetsEntity pets = mock(PetsEntity.class);
		Mockito.when(_petsManager.findById(anyLong())).thenReturn(pets);
		Assertions.assertThat(_appService.findById(ID)).isEqualTo(_mapper.petsEntityToFindPetsByIdOutput(pets));
	}
	
	@Test 
    public void createPets_PetsIsNotNullAndPetsDoesNotExist_StorePets() { 
 
       PetsEntity petsEntity = mock(PetsEntity.class); 
       CreatePetsInput pets = new CreatePetsInput();
   
		TypesEntity types= mock(TypesEntity.class);
        pets.setTypeId(Long.valueOf(ID));
		Mockito.when(_typesManager.findById(
        any(Long.class))).thenReturn(types);
		OwnersEntity owners= mock(OwnersEntity.class);
        pets.setOwnerId(Long.valueOf(ID));
		Mockito.when(_ownersManager.findById(
        any(Long.class))).thenReturn(owners);
		
        Mockito.when(_mapper.createPetsInputToPetsEntity(any(CreatePetsInput.class))).thenReturn(petsEntity); 
        Mockito.when(_petsManager.create(any(PetsEntity.class))).thenReturn(petsEntity);
      
        Assertions.assertThat(_appService.create(pets)).isEqualTo(_mapper.petsEntityToCreatePetsOutput(petsEntity)); 
    } 
	@Test
	public void createPets_PetsIsNotNullAndPetsDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		CreatePetsInput pets = mock(CreatePetsInput.class);
		
		Mockito.when(_mapper.createPetsInputToPetsEntity(any(CreatePetsInput.class))).thenReturn(null); 
		Assertions.assertThat(_appService.create(pets)).isEqualTo(null);
	}
	
	@Test
	public void createPets_PetsIsNotNullAndPetsDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {

		CreatePetsInput pets = new CreatePetsInput();
	    
        pets.setTypeId(Long.valueOf(ID));
     
		Mockito.when(_typesManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.create(pets)).isEqualTo(null);
    }
    
    @Test
	public void updatePets_PetsIsNotNullAndPetsDoesNotExistAndChildIsNullAndChildIsMandatory_ReturnNull() {

		UpdatePetsInput pets = mock(UpdatePetsInput.class);
		PetsEntity petsEntity = mock(PetsEntity.class); 
		
		Mockito.when(_mapper.updatePetsInputToPetsEntity(any(UpdatePetsInput.class))).thenReturn(petsEntity); 
		Assertions.assertThat(_appService.update(ID,pets)).isEqualTo(null);
	}
	
	@Test
	public void updatePets_PetsIsNotNullAndPetsDoesNotExistAndChildIsNotNullAndChildIsMandatoryAndFindByIdIsNull_ReturnNull() {
		
		UpdatePetsInput pets = new UpdatePetsInput();
        pets.setTypeId(Long.valueOf(ID));
		
     
		Mockito.when(_typesManager.findById(any(Long.class))).thenReturn(null);
		Assertions.assertThat(_appService.update(ID,pets)).isEqualTo(null);
	}

		
	@Test
	public void updatePets_PetsIdIsNotNullAndIdExists_ReturnUpdatedPets() {

		PetsEntity petsEntity = mock(PetsEntity.class);
		UpdatePetsInput pets= mock(UpdatePetsInput.class);
		
		Mockito.when(_mapper.updatePetsInputToPetsEntity(any(UpdatePetsInput.class))).thenReturn(petsEntity);
		Mockito.when(_petsManager.update(any(PetsEntity.class))).thenReturn(petsEntity);
		Assertions.assertThat(_appService.update(ID,pets)).isEqualTo(_mapper.petsEntityToUpdatePetsOutput(petsEntity));
	}
    
	@Test
	public void deletePets_PetsIsNotNullAndPetsExists_PetsRemoved() {

		PetsEntity pets= new PetsEntity();
		OwnersEntity owners = mock(OwnersEntity.class);
		TypesEntity types = mock(TypesEntity.class);
		pets.setOwners(owners);
		pets.setTypes(types);
		Mockito.when(_petsManager.findById(anyLong())).thenReturn(pets);
		
		_appService.delete(ID); 
		verify(_petsManager).delete(pets);
	}
	
	@Test
	public void find_ListIsEmpty_ReturnList() throws Exception {

		List<PetsEntity> list = new ArrayList<>();
		Page<PetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindPetsByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();

		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
		Mockito.when(_petsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void find_ListIsNotEmpty_ReturnList() throws Exception {

		List<PetsEntity> list = new ArrayList<>();
		PetsEntity pets = mock(PetsEntity.class);
		list.add(pets);
    	Page<PetsEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindPetsByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
		output.add(_mapper.petsEntityToFindPetsByIdOutput(pets));
		
		Mockito.when(_appService.search(any(SearchCriteria.class))).thenReturn(new BooleanBuilder());
    	Mockito.when(_petsManager.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void filterPets_ListIsNotEmptyAndPetExistsAgainstOwner_ReturnList() {
		
		List<FindPetsByIdOutput> list = new ArrayList<>();
		FindPetsByIdOutput pets = new FindPetsByIdOutput();
		
		OwnersEntity owners = mock(OwnersEntity.class);
		pets.setOwnerId(2L);
		
		list.add(pets);
		
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(owners);
		Assertions.assertThat(_appService.filterPets(list, 2L)).isEqualTo(list);
	}
	
	@Test
	public void filterPets_ListIsNotEmptyAndPetDoesNotExistsAgainstOwner_ReturnEmptyList() {
		
		List<FindPetsByIdOutput> list = new ArrayList<>();
		FindPetsByIdOutput pets = mock(FindPetsByIdOutput.class);
		OwnersEntity owners = mock(OwnersEntity.class);
		
		list.add(pets);
		
		Mockito.when(_ownersManager.findById(anyLong())).thenReturn(owners);
		Assertions.assertThat(_appService.filterPets(list, 2L)).isEqualTo(new ArrayList<>());
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QPetsEntity pets = QPetsEntity.petsEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
        map.put("name",searchFields);
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
         builder.and(pets.name.eq("xyz"));
		Assertions.assertThat(_appService.searchKeyValuePair(pets,map,searchMap)).isEqualTo(builder);
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
        list.add("name");
		_appService.checkProperties(list);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QPetsEntity pets = QPetsEntity.petsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setFieldName("name");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
        builder.or(pets.name.eq("xyz"));
        Mockito.doNothing().when(_appService).checkProperties(any(List.class));
		Mockito.doReturn(builder).when(_appService).searchKeyValuePair(any(QPetsEntity.class), any(HashMap.class), any(HashMap.class));
        
		Assertions.assertThat(_appService.search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.search(null)).isEqualTo(null);
	}
   
   //Types
	@Test
	public void GetTypes_IfPetsIdAndTypesIdIsNotNullAndPetsExists_ReturnTypes() {
		PetsEntity pets = mock(PetsEntity.class);
		TypesEntity types = mock(TypesEntity.class);

		Mockito.when(_petsManager.findById(anyLong())).thenReturn(pets);
		Mockito.when(_petsManager.getTypes(anyLong())).thenReturn(types);
		Assertions.assertThat(_appService.getTypes(ID)).isEqualTo(_mapper.typesEntityToGetTypesOutput(types, pets));
	}

	@Test 
	public void GetTypes_IfPetsIdAndTypesIdIsNotNullAndPetsDoesNotExist_ReturnNull() {
		Mockito.when(_petsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.getTypes(ID)).isEqualTo(null);
	}
   
   //Owners
	@Test
	public void GetOwners_IfPetsIdAndOwnersIdIsNotNullAndPetsExists_ReturnOwners() {
		PetsEntity pets = mock(PetsEntity.class);
		OwnersEntity owners = mock(OwnersEntity.class);

		Mockito.when(_petsManager.findById(anyLong())).thenReturn(pets);
		Mockito.when(_petsManager.getOwners(anyLong())).thenReturn(owners);
		Assertions.assertThat(_appService.getOwners(ID)).isEqualTo(_mapper.ownersEntityToGetOwnersOutput(owners, pets));
	}

	@Test 
	public void GetOwners_IfPetsIdAndOwnersIdIsNotNullAndPetsDoesNotExist_ReturnNull() {
		Mockito.when(_petsManager.findById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.getOwners(ID)).isEqualTo(null);
	}
	
	@Test
	public void ParseVisitsJoinColumn_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
	    Map<String,String> joinColumnMap = new HashMap<String,String>();
		String keyString= "15";
		joinColumnMap.put("petId", keyString);
		Assertions.assertThat(_appService.parseVisitsJoinColumn(keyString)).isEqualTo(joinColumnMap);
	}
}


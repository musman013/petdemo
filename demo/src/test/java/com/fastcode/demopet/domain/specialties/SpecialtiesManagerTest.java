package com.fastcode.demopet.domain.specialties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.demopet.domain.model.SpecialtiesEntity;
import com.fastcode.demopet.domain.irepository.IVetSpecialtiesRepository;
import com.fastcode.demopet.domain.irepository.ISpecialtiesRepository;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpecialtiesManagerTest {

	@InjectMocks
	SpecialtiesManager _specialtiesManager;
	
	@Mock
	ISpecialtiesRepository  _specialtiesRepository;
    
    @Mock
	IVetSpecialtiesRepository  _vetSpecialtiesRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_specialtiesManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findSpecialtiesById_IdIsNotNullAndIdExists_ReturnSpecialties() {
		SpecialtiesEntity specialties =mock(SpecialtiesEntity.class);

        Optional<SpecialtiesEntity> dbSpecialties = Optional.of((SpecialtiesEntity) specialties);
		Mockito.<Optional<SpecialtiesEntity>>when(_specialtiesRepository.findById(anyLong())).thenReturn(dbSpecialties);
		Assertions.assertThat(_specialtiesManager.findById(ID)).isEqualTo(specialties);
	}

	@Test 
	public void findSpecialtiesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<SpecialtiesEntity>>when(_specialtiesRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(_specialtiesManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createSpecialties_SpecialtiesIsNotNullAndSpecialtiesDoesNotExist_StoreSpecialties() {

		SpecialtiesEntity specialties =mock(SpecialtiesEntity.class);
		Mockito.when(_specialtiesRepository.save(any(SpecialtiesEntity.class))).thenReturn(specialties);
		Assertions.assertThat(_specialtiesManager.create(specialties)).isEqualTo(specialties);
	}

	@Test
	public void deleteSpecialties_SpecialtiesExists_RemoveSpecialties() {

		SpecialtiesEntity specialties =mock(SpecialtiesEntity.class);
		_specialtiesManager.delete(specialties);
		verify(_specialtiesRepository).delete(specialties);
	}

	@Test
	public void updateSpecialties_SpecialtiesIsNotNullAndSpecialtiesExists_UpdateSpecialties() {
		
		SpecialtiesEntity specialties =mock(SpecialtiesEntity.class);
		Mockito.when(_specialtiesRepository.save(any(SpecialtiesEntity.class))).thenReturn(specialties);
		Assertions.assertThat(_specialtiesManager.update(specialties)).isEqualTo(specialties);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<SpecialtiesEntity> specialties = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_specialtiesRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(specialties);
		Assertions.assertThat(_specialtiesManager.findAll(predicate,pageable)).isEqualTo(specialties);
	}
	
}

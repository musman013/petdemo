package com.fastcode.demopet.domain.invoices;

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

import com.fastcode.demopet.domain.model.InvoicesEntity;
import com.fastcode.demopet.domain.irepository.IVisitsRepository;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.irepository.IInvoicesRepository;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class InvoicesManagerTest {

	@InjectMocks
	InvoicesManager _invoicesManager;
	
	@Mock
	IInvoicesRepository  _invoicesRepository;
    
    @Mock
	IVisitsRepository  _visitsRepository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_invoicesManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findInvoicesById_IdIsNotNullAndIdExists_ReturnInvoices() {
		InvoicesEntity invoices =mock(InvoicesEntity.class);

        Optional<InvoicesEntity> dbInvoices = Optional.of((InvoicesEntity) invoices);
		Mockito.<Optional<InvoicesEntity>>when(_invoicesRepository.findById(anyLong())).thenReturn(dbInvoices);
		Assertions.assertThat(_invoicesManager.findById(ID)).isEqualTo(invoices);
	}

	@Test 
	public void findInvoicesById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<InvoicesEntity>>when(_invoicesRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(_invoicesManager.findById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createInvoices_InvoicesIsNotNullAndInvoicesDoesNotExist_StoreInvoices() {

		InvoicesEntity invoices =mock(InvoicesEntity.class);
		Mockito.when(_invoicesRepository.save(any(InvoicesEntity.class))).thenReturn(invoices);
		Assertions.assertThat(_invoicesManager.create(invoices)).isEqualTo(invoices);
	}

	@Test
	public void deleteInvoices_InvoicesExists_RemoveInvoices() {

		InvoicesEntity invoices =mock(InvoicesEntity.class);
		_invoicesManager.delete(invoices);
		verify(_invoicesRepository).delete(invoices);
	}

	@Test
	public void updateInvoices_InvoicesIsNotNullAndInvoicesExists_UpdateInvoices() {
		
		InvoicesEntity invoices =mock(InvoicesEntity.class);
		Mockito.when(_invoicesRepository.save(any(InvoicesEntity.class))).thenReturn(invoices);
		Assertions.assertThat(_invoicesManager.update(invoices)).isEqualTo(invoices);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<InvoicesEntity> invoices = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_invoicesRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(invoices);
		Assertions.assertThat(_invoicesManager.findAll(predicate,pageable)).isEqualTo(invoices);
	}
	
    //Visits
	@Test
	public void getVisits_if_InvoicesIdIsNotNull_returnVisits() {

		InvoicesEntity invoices = mock(InvoicesEntity.class);
		VisitsEntity visits = mock(VisitsEntity.class);
		
        Optional<InvoicesEntity> dbInvoices = Optional.of((InvoicesEntity) invoices);
		Mockito.<Optional<InvoicesEntity>>when(_invoicesRepository.findById(anyLong())).thenReturn(dbInvoices);
		Mockito.when(invoices.getVisits()).thenReturn(visits);
		Assertions.assertThat(_invoicesManager.getVisits(ID)).isEqualTo(visits);

	}
	
}

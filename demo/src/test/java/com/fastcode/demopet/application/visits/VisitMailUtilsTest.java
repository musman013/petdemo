package com.fastcode.demopet.application.visits;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.visits.VisitsManager;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.FindEmailTemplateByNameOutput;
import com.fastcode.demopet.emailbuilder.application.mail.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitMailUtilsTest {


	@InjectMocks
	@Spy
	private VisitMailUtils visitMail;

	@Mock
	private EmailService _mailAppservice;

	@Mock
	private EmailTemplateAppService _emailAppservice;

	@Mock
	private VisitsManager _visitsManager;
	
	@Mock 
	private EmailTemplateAppService _emailTemplateAppService;
	
	@Mock
	private SchedulerFactoryBean schedulerFactoryBean;

	@Mock
	private Scheduler scheduler;
	
	@Mock
	private JobExecutionContext executionContext;
	
	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

    private static Long ID=15L;

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(visitMail);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		Mockito.when(visitMail.getScheduler()).thenReturn(scheduler);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void buildVisitConfirmationMail_IdIsNotNullAndEmailTemplateExist_ReturnNothing() throws IOException {

		FindEmailTemplateByNameOutput emailTemplate = Mockito.mock(FindEmailTemplateByNameOutput.class);
		VisitsEntity visits = new VisitsEntity();
		visits.setId(2L);
		visits.setVisitDate(new Date());
		OwnersEntity owners = new OwnersEntity();
		UserEntity user = new UserEntity();
		user.setId(2L);
		owners.setUser(user);
		owners.setId(2L);
		PetsEntity pets = new PetsEntity();
		pets.setOwners(owners);
		visits.setPets(pets);
		VetsEntity vets = new VetsEntity();
		vets.setUser(user);
		visits.setVets(vets);
		
		Mockito.when(_visitsManager.findById(anyLong())).thenReturn(visits);
		Mockito.when(_emailAppservice.findByName(anyString())).thenReturn(emailTemplate);
		Mockito.doNothing().when(_mailAppservice).sendVisitEmail(any(FindEmailTemplateByNameOutput.class), any(HashMap.class));
		
		visitMail.buildVisitConfirmationMail(2L); 
		verify(_mailAppservice, times(1)).sendVisitEmail(any(FindEmailTemplateByNameOutput.class), any(HashMap.class));
	}


	@Test
	public void scheduleReminderJob_VisitIsValid_ReturnNothing()
	{
		
		visitMail.scheduleReminderJob(2L); 
		verify(visitMail, times(1)).scheduleReminderJob(anyLong());
	}

}

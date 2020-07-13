package com.fastcode.demopet.application.visits;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.visits.IVisitsManager;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.FindEmailTemplateByNameOutput;
import com.fastcode.demopet.emailbuilder.application.mail.EmailService;


@Component
public class VisitMailUtils {
	
	@Autowired
	private IVisitsManager _visitsManager;
	
	@Autowired
	private EmailService _mailAppservice;
	
	@Autowired
	EmailTemplateAppService _emailAppservice;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	private Scheduler scheduler;

	public Scheduler getScheduler() throws SchedulerException
	{
		scheduler= schedulerFactoryBean.getScheduler();
		//scheduler.start();
		return scheduler;
	}
	
	
	public void scheduleReminderJob(Long visitId)
	{
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		LocalDateTime localDate = new Date().toInstant().atZone(defaultZoneId).toLocalDateTime();
		Date dayBeforeVisit =Date.from(localDate.plusMinutes(1).atZone(defaultZoneId).toInstant());

//		if(new Date().before(dayBeforeVisit)) {
		
		String jobKey = "ReminderJob" + visitId;
		String jobGroup = "visitReminder";
		
		String triggerKey = "ReminderTrigger" + visitId;
		String triggerGroup = "visitReminder";
		try {
			if (!(getScheduler().checkExists(new JobKey(jobKey, jobGroup)))) {
				Class<? extends Job> className = (Class<? extends Job>) Class.forName("com.fastcode.demopet.scheduler.jobs.visitReminderEmailJob");
				JobDetail jobDetails = JobBuilder.newJob(className)
						.withDescription("visit reminder email")
						.withIdentity(jobKey, jobGroup).build();
				jobDetails.getJobDataMap().put("visitId", String.valueOf(visitId));
			
				Trigger trigger = TriggerBuilder.newTrigger()
					    .withIdentity(triggerKey, triggerGroup)
					    .startAt(dayBeforeVisit) // some Date
					    .forJob(jobKey, jobGroup) // identify job with name, group strings
					    .build();
				getScheduler().scheduleJob(jobDetails, trigger);
			} 
			else {
				throw new EntityNotFoundException ("Job key already exists");
			}
		} catch (ClassNotFoundException | SchedulerException e) {
			e.printStackTrace();
		}
//		}
	}
	
	public void buildVisitConfirmationMail(Long visitId)
	{
		FindEmailTemplateByNameOutput emailTemplate = _emailAppservice.findByName("Template_1");
		Map<String,String> map = new HashMap<String,String>();

		VisitsEntity visit = _visitsManager.findById(Long.valueOf(visitId));
		OwnersEntity owner = visit.getPets().getOwners();
		UserEntity user = owner.getUser();
		PetsEntity pet = visit.getPets();
		UserEntity vet = visit.getVets().getUser();
		
		emailTemplate.setTo(user.getEmailAddress());

		map.put("petOwner_firstName", user.getFirstName());
		map.put("visitPetName", pet.getName());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss:S aa");
		String formattedDate = dateFormat.format(visit.getVisitDate()).toString();
		System.out.println("visit Date " +visit.getVisitDate().toString());
		System.out.println("formatted Date " + formattedDate);
		map.put("visitDateTime",formattedDate);
		map.put("visitVetName", vet.getFirstName() + " " + vet.getLastName());
		
		try {
			_mailAppservice.sendVisitEmail(emailTemplate,map);
		} catch (IOException e1) {
			System.out.println(" Error while sending email");
			e1.printStackTrace();
		}
	}
	

}

package com.fastcode.demopet.scheduler.jobs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.owners.IOwnersManager;
import com.fastcode.demopet.domain.pets.IPetsManager;
import com.fastcode.demopet.domain.visits.IVisitsManager;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.EmailTemplateAppService;
import com.fastcode.demopet.emailbuilder.application.emailtemplate.dto.FindEmailTemplateByNameOutput;
import com.fastcode.demopet.emailbuilder.application.emailvariable.EmailVariableAppService;
import com.fastcode.demopet.emailbuilder.application.mail.EmailService;

public class visitConfirmationEmailJob implements Job {
	@Autowired
	IVisitsManager _visitManager;

	@Autowired
	IPetsManager _petsManager;

	@Autowired
	IOwnersManager _ownersManager;

	@Autowired
	Environment env;

	@Autowired
	EmailTemplateAppService _emailAppservice;

	@Autowired
	EmailVariableAppService _emailVariableAppservice;

	@Autowired
	private EmailService _mailAppservice;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey key = context.getJobDetail().getKey();

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		System.out.println("Instance " + key );

		FindEmailTemplateByNameOutput emailTemplate = _emailAppservice.findByName("Template_1");
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(env.getProperty("fastCode.offset.default")), Integer.parseInt(env.getProperty("fastCode.limit.default")), Sort.by("id"));

		Map<String,String> map = new HashMap<String,String>();
		// SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject("");
		// List<FindEmailVariableByIdOutput> list=new ArrayList<FindEmailVariableByIdOutput>();
		// try {
		// list = _emailVariableAppservice.find(searchCriteria, pageable);
		//
		// } catch (Exception e2) {
		// System.out.println(" Error while fetching list");
		// e2.printStackTrace();
		// }
		//

		// for (FindEmailVariableByIdOutput tag: list) {
		// map.put(tag.getPropertyName(),"");
		// }

		Object obj = dataMap.get("visitId");
		String visitId = String.valueOf(obj);
		VisitsEntity visit = _visitManager.findById(Long.valueOf(visitId));
		OwnersEntity owner = visit.getPets().getOwners();
		UserEntity user = owner.getUser();
		PetsEntity pet = visit.getPets();
		UserEntity vet = visit.getVets().getUser();
		emailTemplate.setTo(user.getEmailAddress());

//		map.put("Pet owner EmailAddress", user.getEmailAddress());
		map.put("petOwner_firstName", user.getFirstName());
		map.put("visitPetName", pet.getName());
		map.put("visitDateTime",visit.getVisitDate().toString());
		map.put("visitVetName", vet.getFirstName() + " " + vet.getLastName());
		
//		map.put("visit link","https://localhost:4200/visit/" + visitId);
		try {
			_mailAppservice.sendVisitEmail(emailTemplate,map);


		} catch (IOException e1) {
			System.out.println(" Error while sending email");
			e1.printStackTrace();

		}
	}
}
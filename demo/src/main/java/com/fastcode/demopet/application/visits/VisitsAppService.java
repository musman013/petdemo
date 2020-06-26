package com.fastcode.demopet.application.visits;

import com.fastcode.demopet.application.visits.dto.*;
import com.fastcode.demopet.domain.visits.IVisitsManager;
import com.fastcode.demopet.domain.model.QVisitsEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.model.VisitsEntity;
import com.fastcode.demopet.domain.owners.OwnersManager;
import com.fastcode.demopet.domain.pets.PetsManager;
import com.fastcode.demopet.domain.vets.VetsManager;
import com.fastcode.demopet.domain.model.OwnersEntity;
import com.fastcode.demopet.domain.model.PetsEntity;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Service
@Validated
public class VisitsAppService implements IVisitsAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IVisitsManager _visitsManager;
	
    @Autowired
	private PetsManager _petsManager;
    
    @Autowired
	private OwnersManager _ownersManager;
    
    @Autowired
    private VetsManager _vetsManager;
    
	@Autowired
	private IVisitsMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	private Scheduler scheduler;

	public Scheduler getScheduler() throws SchedulerException
	{
		scheduler= schedulerFactoryBean.getScheduler();
		//scheduler.start();
		return scheduler;
	}

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateVisitsOutput create(CreateVisitsInput input) {

		VisitsEntity visits = mapper.createVisitsInputToVisitsEntity(input);
		visits.setStatus(Status.CREATED);
	  	if(input.getPetId()!=null) {
			PetsEntity foundPets = _petsManager.findById(input.getPetId());
			if(foundPets!=null) {
				foundPets.addVisits(visits);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	  	
	  	if(input.getVetId() !=null)
	  	{
	  		VetsEntity foundVets = _vetsManager.findById(input.getVetId());
			if(foundVets !=null) {
				foundVets.addVisits(visits);
			}
			else {
				return null;
			}
	  	}
		
		VisitsEntity createdVisits = _visitsManager.create(visits);
		scheduleVisitConfirmationJob(createdVisits.getId());
		scheduleReminderJob(createdVisits.getId());

		return mapper.visitsEntityToCreateVisitsOutput(createdVisits);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateVisitsOutput update(Long  visitsId, UpdateVisitsInput input) {

		VisitsEntity visits = mapper.updateVisitsInputToVisitsEntity(input);
	  	if(input.getPetId()!=null) {
			PetsEntity foundPets = _petsManager.findById(input.getPetId());
			if(foundPets!=null) {
				visits.setPets(foundPets);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		
		VisitsEntity updatedVisits = _visitsManager.update(visits);
		
		return mapper.visitsEntityToUpdateVisitsOutput(updatedVisits);
	}
	
	public void scheduleReminderJob(Long visitId)
	{
		ZoneId defaultZoneId = ZoneId.systemDefault();
		
		LocalDateTime localDate = new Date().toInstant().atZone(defaultZoneId).toLocalDateTime();
		Date dayBeforeVisit =Date.from(localDate.plusMinutes(1).atZone(defaultZoneId).toInstant());

		if(new Date().before(dayBeforeVisit)) {
		
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
		}
	}
	
	public void scheduleVisitConfirmationJob(Long visitId)
	{
		String jobKey = "confirmationjob" + visitId;
		String jobGroup = "visitConfirmation";
		
		String triggerKey = "confirmationTrigger" + visitId;
		String triggerGroup = "visitConfirmation";
		try {
			if (!(getScheduler().checkExists(new JobKey(jobKey, jobGroup)))) {
				Class<? extends Job> className = (Class<? extends Job>) Class.forName("com.fastcode.demopet.scheduler.jobs.visitConfirmationEmailJob");
				JobDetail jobDetails = JobBuilder.newJob(className)
						.withDescription("visit confirmation email")
						.withIdentity(jobKey, jobGroup).build();
				jobDetails.getJobDataMap().put("visitId", String.valueOf(visitId));
			
				Trigger trigger = TriggerBuilder.newTrigger()
					    .withIdentity(triggerKey, triggerGroup)
					    .startAt(new Date()) // some Date
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
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long visitsId) {

		VisitsEntity existing = _visitsManager.findById(visitsId) ; 
		
		PetsEntity pets = existing.getPets();
		pets.removeVisits(existing);
 
		_visitsManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindVisitsByIdOutput findById(Long visitsId) {

		VisitsEntity foundVisits = _visitsManager.findById(visitsId);
		if (foundVisits == null)  
			return null ; 
 	   
 	    FindVisitsByIdOutput output=mapper.visitsEntityToFindVisitsByIdOutput(foundVisits); 
		return output;
	}
	
	public FindVisitsByIdOutput changeStatus(Long visitsId, UpdateVisitStatus input)
	{
		VisitsEntity foundVisits = _visitsManager.findById(visitsId);
		
		foundVisits.setStatus(input.getStatus());
		if(input.getVisitNotes() !=null)
		{
			foundVisits.setVisitNotes(input.getVisitNotes());
		}

		return mapper.visitsEntityToFindVisitsByIdOutput(_visitsManager.update(foundVisits));
		
	}
    //Pets
	// ReST API Call - GET /visits/1/pets
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public GetPetsOutput getPets(Long visitsId) {

		VisitsEntity foundVisits = _visitsManager.findById(visitsId);
		if (foundVisits == null) {
			logHelper.getLogger().error("There does not exist a visits wth a id=%s", visitsId);
			return null;
		}
		PetsEntity re = _visitsManager.getPets(visitsId);
		return mapper.petsEntityToGetPetsOutput(re, foundVisits);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindVisitsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<VisitsEntity> foundVisits = _visitsManager.findAll(search(search), pageable);
		List<VisitsEntity> visitsList = foundVisits.getContent();
		Iterator<VisitsEntity> visitsIterator = visitsList.iterator(); 
		List<FindVisitsByIdOutput> output = new ArrayList<>();

		while (visitsIterator.hasNext()) {
			output.add(mapper.visitsEntityToFindVisitsByIdOutput(visitsIterator.next()));
		}
		return output;
	}
    
    public List<FindVisitsByIdOutput> filterVisits(List<FindVisitsByIdOutput> list, Long userId)
    {
    	List<FindVisitsByIdOutput> filteredList = new ArrayList<FindVisitsByIdOutput>();
    	OwnersEntity owner = _ownersManager.findById(userId);
    	VetsEntity vet = _vetsManager.findById(userId);
    	for(FindVisitsByIdOutput obj : list)
    	{
    		VisitsEntity visit = _visitsManager.findById(obj.getId());
    		
    		if(owner !=null) {
    			PetsEntity pet = _petsManager.findById(obj.getPetId());
    		if(userId == pet.getOwners().getId()) {
    			filteredList.add(obj);
    		}
    		}
    		if(vet !=null) {
    			if(userId == obj.getVetId()) {
        			filteredList.add(obj);
        		}
    		}
    	}

    	return filteredList;
    }
    
//    public List<FindVisitsByIdOutput> filterVetsVisits(List<FindVisitsByIdOutput> list, Long userId)
//    {
//    	List<FindVisitsByIdOutput> filteredList = new ArrayList<FindVisitsByIdOutput>();
//    	VetsEntity vet = _vetsManager.findById(userId);
//    	for(FindVisitsByIdOutput obj : list)
//    	{
//    		VisitsEntity visit = _visitsManager.findById(obj.getId());
//    		
//    		if(obj.get) {
//    			filteredList.add(obj);
//    		}
//    	}
//    	
//    	return filteredList;
//    }
   
   

	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QVisitsEntity visits= QVisitsEntity.visitsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(visits, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("petId") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("invoices") ||
				list.get(i).replace("%20","").trim().equals("pets") ||
				list.get(i).replace("%20","").trim().equals("visitDate")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QVisitsEntity visits, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(visits.description.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(visits.description.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(visits.description.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("visitDate")) {
				if(details.getValue().getOperator().equals("equals") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(visits.visitDate.eq(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && SearchUtils.stringToDate(details.getValue().getSearchValue()) !=null)
					builder.and(visits.visitDate.ne(SearchUtils.stringToDate(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   Date startDate= SearchUtils.stringToDate(details.getValue().getStartingValue());
				   Date endDate= SearchUtils.stringToDate(details.getValue().getEndingValue());
				   if(startDate!=null && endDate!=null)	 
					   builder.and(visits.visitDate.between(startDate,endDate));
				   else if(endDate!=null)
					   builder.and(visits.visitDate.loe(endDate));
                   else if(startDate!=null)
                	   builder.and(visits.visitDate.goe(startDate));  
                 }
                   
			}
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("petId")) {
		    builder.and(visits.pets.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	
	public Map<String,String> parseInvoicesJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("visitId", keysString);
		return joinColumnMap;
	}
    
	
}



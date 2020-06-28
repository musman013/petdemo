package com.fastcode.demopet.reporting.application.permalink;

import com.fastcode.demopet.application.authorization.user.IUserAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchFields;
import com.fastcode.demopet.domain.model.QPermalinkEntity;
import com.fastcode.demopet.domain.model.PermalinkEntity;
import com.fastcode.demopet.reporting.application.permalink.dto.*;
import com.fastcode.demopet.reporting.application.reportuser.dto.FindReportuserByIdOutput;
import com.fastcode.demopet.reporting.domain.permalink.IPermalinkManager;
import com.querydsl.core.BooleanBuilder;

import java.util.*;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
@Validated
public class PermalinkAppService implements IPermalinkAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IPermalinkManager _permalinkManager;

	@Autowired
	private PermalinkMapper mapper;
	
	@Autowired
	private IUserAppService _userAppService;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreatePermalinkOutput create(CreatePermalinkInput input) {
		
		PermalinkEntity permalink = mapper.createPermalinkInputToPermalinkEntity(input);
		permalink.setId(UUID.randomUUID());
		permalink.setUserId(_userAppService.getUser().getId());
		PermalinkEntity createdPermalink = _permalinkManager.create(permalink);
		
		return mapper.permalinkEntityToCreatePermalinkOutput(createdPermalink);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdatePermalinkOutput update(UUID  permalinkId, UpdatePermalinkInput input) {

		PermalinkEntity permalink = mapper.updatePermalinkInputToPermalinkEntity(input);
		
		PermalinkEntity updatedPermalink = _permalinkManager.update(permalink);
		
		return mapper.permalinkEntityToUpdatePermalinkOutput(updatedPermalink);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(UUID permalinkId) {

		PermalinkEntity existing = _permalinkManager.findById(permalinkId) ; 
		_permalinkManager.delete(existing);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindPermalinkByIdOutput findById(UUID permalinkId) {

		PermalinkEntity foundPermalink = _permalinkManager.findById(permalinkId);
		if (foundPermalink == null)  
			return null ; 
 	   
 	    FindPermalinkByIdOutput output=mapper.permalinkEntityToFindPermalinkByIdOutput(foundPermalink); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindPermalinkByIdOutput findByResourceAndResourceId(Long resourceId, String resource) {

		PermalinkEntity foundPermalink = _permalinkManager.findByResourceAndresourceId(resourceId, resource);
		if (foundPermalink == null)  
			return null ; 
 	   
 	    FindPermalinkByIdOutput output = mapper.permalinkEntityToFindPermalinkByIdOutput(foundPermalink); 
		return output;
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindPermalinkByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<PermalinkEntity> foundPermalink = _permalinkManager.findAll(search(search), pageable);
		List<PermalinkEntity> permalinkList = foundPermalink.getContent();
		Iterator<PermalinkEntity> permalinkIterator = permalinkList.iterator(); 
		List<FindPermalinkByIdOutput> output = new ArrayList<>();

		while (permalinkIterator.hasNext()) {
			output.add(mapper.permalinkEntityToFindPermalinkByIdOutput(permalinkIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QPermalinkEntity permalink= QPermalinkEntity.permalinkEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(permalink, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("authentication") ||
				list.get(i).replace("%20","").trim().equals("description") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("refreshRate") ||
				list.get(i).replace("%20","").trim().equals("rendering") ||
				list.get(i).replace("%20","").trim().equals("resource") ||
				list.get(i).replace("%20","").trim().equals("resourceId") ||
				list.get(i).replace("%20","").trim().equals("toolbar")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QPermalinkEntity permalink, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("authentication")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permalink.authentication.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permalink.authentication.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permalink.authentication.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("description")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(permalink.description.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(permalink.description.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
			if(details.getKey().replace("%20","").trim().equals("refreshRate")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(permalink.refreshRate.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(permalink.refreshRate.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(permalink.refreshRate.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(permalink.refreshRate.goe(Long.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(permalink.refreshRate.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
            if(details.getKey().replace("%20","").trim().equals("rendering")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permalink.rendering.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permalink.rendering.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permalink.rendering.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("resource")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permalink.resource.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permalink.resource.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permalink.resource.ne(details.getValue().getSearchValue()));
			}
			if(details.getKey().replace("%20","").trim().equals("resourceId")) {
				if(details.getValue().getOperator().equals("equals") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(permalink.resourceId.eq(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && StringUtils.isNumeric(details.getValue().getSearchValue()))
					builder.and(permalink.resourceId.ne(Long.valueOf(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("range"))
				{
				   if(StringUtils.isNumeric(details.getValue().getStartingValue()) && StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(permalink.resourceId.between(Long.valueOf(details.getValue().getStartingValue()), Long.valueOf(details.getValue().getEndingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getStartingValue()))
                	   builder.and(permalink.resourceId.goe(Long.valueOf(details.getValue().getStartingValue())));
                   else if(StringUtils.isNumeric(details.getValue().getEndingValue()))
                	   builder.and(permalink.resourceId.loe(Long.valueOf(details.getValue().getEndingValue())));
				}
			}
			if(details.getKey().replace("%20","").trim().equals("toolbar")) {
				if(details.getValue().getOperator().equals("equals") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(permalink.toolbar.eq(Boolean.parseBoolean(details.getValue().getSearchValue())));
				else if(details.getValue().getOperator().equals("notEqual") && (details.getValue().getSearchValue().equalsIgnoreCase("true") || details.getValue().getSearchValue().equalsIgnoreCase("false")))
					builder.and(permalink.toolbar.ne(Boolean.parseBoolean(details.getValue().getSearchValue())));
			}
		}
		return builder;
	}
	
	
    
	
}



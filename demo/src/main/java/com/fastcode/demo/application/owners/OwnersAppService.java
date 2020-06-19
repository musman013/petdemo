package com.fastcode.demo.application.owners;

import com.fastcode.demo.application.owners.dto.*;
import com.fastcode.demo.domain.owners.IOwnersManager;
import com.fastcode.demo.domain.model.QOwnersEntity;
import com.fastcode.demo.domain.model.OwnersEntity;
import com.fastcode.demo.commons.search.*;
import com.fastcode.demo.commons.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
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
public class OwnersAppService implements IOwnersAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IOwnersManager _ownersManager;

	@Autowired
	private IOwnersMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateOwnersOutput create(CreateOwnersInput input) {

		OwnersEntity owners = mapper.createOwnersInputToOwnersEntity(input);
		
		OwnersEntity createdOwners = _ownersManager.create(owners);
		return mapper.ownersEntityToCreateOwnersOutput(createdOwners);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateOwnersOutput update(Integer  ownersId, UpdateOwnersInput input) {

		OwnersEntity owners = mapper.updateOwnersInputToOwnersEntity(input);
		
		OwnersEntity updatedOwners = _ownersManager.update(owners);
		
		return mapper.ownersEntityToUpdateOwnersOutput(updatedOwners);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer ownersId) {

		OwnersEntity existing = _ownersManager.findById(ownersId) ; 
		
		_ownersManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindOwnersByIdOutput findById(Integer ownersId) {

		OwnersEntity foundOwners = _ownersManager.findById(ownersId);
		if (foundOwners == null)  
			return null ; 
 	   
 	    FindOwnersByIdOutput output=mapper.ownersEntityToFindOwnersByIdOutput(foundOwners); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindOwnersByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<OwnersEntity> foundOwners = _ownersManager.findAll(search(search), pageable);
		List<OwnersEntity> ownersList = foundOwners.getContent();
		Iterator<OwnersEntity> ownersIterator = ownersList.iterator(); 
		List<FindOwnersByIdOutput> output = new ArrayList<>();

		while (ownersIterator.hasNext()) {
			output.add(mapper.ownersEntityToFindOwnersByIdOutput(ownersIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QOwnersEntity owners= QOwnersEntity.ownersEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(owners, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("address") ||
				list.get(i).replace("%20","").trim().equals("city") ||
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("pets")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QOwnersEntity owners, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("address")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(owners.address.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(owners.address.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(owners.address.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("city")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(owners.city.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(owners.city.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(owners.city.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}
	
	
	public Map<String,String> parsePetsJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("ownerId", keysString);
		return joinColumnMap;
	}
    
	
}



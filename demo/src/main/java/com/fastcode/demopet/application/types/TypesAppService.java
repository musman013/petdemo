package com.fastcode.demopet.application.types;

import com.fastcode.demopet.application.types.dto.*;
import com.fastcode.demopet.domain.types.ITypesManager;
import com.fastcode.demopet.domain.model.QTypesEntity;
import com.fastcode.demopet.domain.model.TypesEntity;
import com.fastcode.demopet.commons.search.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;
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
public class TypesAppService implements ITypesAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private ITypesManager _typesManager;

	@Autowired
	private ITypesMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateTypesOutput create(CreateTypesInput input) {

		TypesEntity types = mapper.createTypesInputToTypesEntity(input);
		
		TypesEntity createdTypes = _typesManager.create(types);
		return mapper.typesEntityToCreateTypesOutput(createdTypes);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateTypesOutput update(Long  typesId, UpdateTypesInput input) {

		TypesEntity types = mapper.updateTypesInputToTypesEntity(input);
		
		TypesEntity updatedTypes = _typesManager.update(types);
		
		return mapper.typesEntityToUpdateTypesOutput(updatedTypes);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Long typesId) {

		TypesEntity existing = _typesManager.findById(typesId) ; 
		
		_typesManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindTypesByIdOutput findById(Long typesId) {

		TypesEntity foundTypes = _typesManager.findById(typesId);
		if (foundTypes == null)  
			return null ; 
 	   
 	    FindTypesByIdOutput output=mapper.typesEntityToFindTypesByIdOutput(foundTypes); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindTypesByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<TypesEntity> foundTypes = _typesManager.findAll(search(search), pageable);
		List<TypesEntity> typesList = foundTypes.getContent();
		Iterator<TypesEntity> typesIterator = typesList.iterator(); 
		List<FindTypesByIdOutput> output = new ArrayList<>();

		while (typesIterator.hasNext()) {
			output.add(mapper.typesEntityToFindTypesByIdOutput(typesIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QTypesEntity types= QTypesEntity.typesEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(types, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("name") ||
				list.get(i).replace("%20","").trim().equals("pets")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QTypesEntity types, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(types.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(types.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(types.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}
	
	
	public Map<String,String> parsePetsJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("typeId", keysString);
		return joinColumnMap;
	}
    
	
}



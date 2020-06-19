package com.fastcode.demo.application.vets;

import com.fastcode.demo.application.vets.dto.*;
import com.fastcode.demo.domain.vets.IVetsManager;
import com.fastcode.demo.domain.model.QVetsEntity;
import com.fastcode.demo.domain.model.VetsEntity;
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
public class VetsAppService implements IVetsAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IVetsManager _vetsManager;

	@Autowired
	private IVetsMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateVetsOutput create(CreateVetsInput input) {

		VetsEntity vets = mapper.createVetsInputToVetsEntity(input);
		
		VetsEntity createdVets = _vetsManager.create(vets);
		return mapper.vetsEntityToCreateVetsOutput(createdVets);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public UpdateVetsOutput update(Integer  vetsId, UpdateVetsInput input) {

		VetsEntity vets = mapper.updateVetsInputToVetsEntity(input);
		
		VetsEntity updatedVets = _vetsManager.update(vets);
		
		return mapper.vetsEntityToUpdateVetsOutput(updatedVets);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer vetsId) {

		VetsEntity existing = _vetsManager.findById(vetsId) ; 
		
		_vetsManager.delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public FindVetsByIdOutput findById(Integer vetsId) {

		VetsEntity foundVets = _vetsManager.findById(vetsId);
		if (foundVets == null)  
			return null ; 
 	   
 	    FindVetsByIdOutput output=mapper.vetsEntityToFindVetsByIdOutput(foundVets); 
		return output;
	}
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<FindVetsByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<VetsEntity> foundVets = _vetsManager.findAll(search(search), pageable);
		List<VetsEntity> vetsList = foundVets.getContent();
		Iterator<VetsEntity> vetsIterator = vetsList.iterator(); 
		List<FindVetsByIdOutput> output = new ArrayList<>();

		while (vetsIterator.hasNext()) {
			output.add(mapper.vetsEntityToFindVetsByIdOutput(vetsIterator.next()));
		}
		return output;
	}
	
	public BooleanBuilder search(SearchCriteria search) throws Exception {

		QVetsEntity vets= QVetsEntity.vetsEntity;
		if(search != null) {
			Map<String,SearchFields> map = new HashMap<>();
			for(SearchFields fieldDetails: search.getFields())
			{
				map.put(fieldDetails.getFieldName(),fieldDetails);
			}
			List<String> keysList = new ArrayList<String>(map.keySet());
			checkProperties(keysList);
			return searchKeyValuePair(vets, map,search.getJoinColumns());
		}
		return null;
	}
	
	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
			if(!(
				list.get(i).replace("%20","").trim().equals("id") ||
				list.get(i).replace("%20","").trim().equals("vetspecialties") ||
				list.get(i).replace("%20","").trim().equals("visits")
			)) 
			{
			 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
			}
		}
	}
	
	public BooleanBuilder searchKeyValuePair(QVetsEntity vets, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		return builder;
	}
	
	
	public Map<String,String> parseVetSpecialtiesJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("vetId", keysString);
		return joinColumnMap;
	}
	public Map<String,String> parseVisitsJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("vetId", keysString);
		return joinColumnMap;
	}
    
	
}



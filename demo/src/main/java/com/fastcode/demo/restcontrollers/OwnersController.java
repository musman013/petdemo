package com.fastcode.demo.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.commons.search.SearchUtils;
import com.fastcode.demo.commons.application.OffsetBasedPageRequest;
import com.fastcode.demo.commons.domain.EmptyJsonResponse;
import com.fastcode.demo.application.owners.OwnersAppService;
import com.fastcode.demo.application.owners.dto.*;
import com.fastcode.demo.application.pets.PetsAppService;
import com.fastcode.demo.application.pets.dto.FindPetsByIdOutput;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demo.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/owners")
public class OwnersController {

	@Autowired
	private OwnersAppService _ownersAppService;
    
    @Autowired
	private PetsAppService  _petsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public OwnersController(OwnersAppService ownersAppService, PetsAppService petsAppService,
	 LoggingHelper helper) {
		super();
		this._ownersAppService = ownersAppService;
    	this._petsAppService = petsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateOwnersOutput> create(@RequestBody @Valid CreateOwnersInput owners) {
		CreateOwnersOutput output=_ownersAppService.create(owners);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete owners ------------
	@PreAuthorize("hasAnyAuthority('OWNERSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindOwnersByIdOutput output = _ownersAppService.findById(Integer.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a owners with a id=%s", id)));
	
    _ownersAppService.delete(Integer.valueOf(id));
    }
    
	
	// ------------ Update owners ------------
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateOwnersOutput> update(@PathVariable String id, @RequestBody @Valid UpdateOwnersInput owners) {
	    FindOwnersByIdOutput currentOwners = _ownersAppService.findById(Integer.valueOf(id));
		Optional.ofNullable(currentOwners).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Owners with id=%s not found.", id)));
		
		
		owners.setVersion(currentOwners.getVersion());
    	
	    return new ResponseEntity(_ownersAppService.update(Integer.valueOf(id),owners), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindOwnersByIdOutput> findById(@PathVariable String id) {
	
    	FindOwnersByIdOutput output = _ownersAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_ownersAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_READ')")
	@RequestMapping(value = "/{id}/pets", method = RequestMethod.GET)
	public ResponseEntity getPets(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_ownersAppService.parsePetsJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindPetsByIdOutput> output = _petsAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 


}


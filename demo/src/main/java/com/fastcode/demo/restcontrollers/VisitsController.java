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
import com.fastcode.demo.application.visits.VisitsAppService;
import com.fastcode.demo.application.visits.dto.*;
import com.fastcode.demo.application.pets.PetsAppService;
import com.fastcode.demo.application.vets.VetsAppService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demo.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/visits")
public class VisitsController {

	@Autowired
	private VisitsAppService _visitsAppService;
    
    @Autowired
	private PetsAppService  _petsAppService;
    
    @Autowired
	private VetsAppService  _vetsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public VisitsController(VisitsAppService visitsAppService, PetsAppService petsAppService, VetsAppService vetsAppService,
	 LoggingHelper helper) {
		super();
		this._visitsAppService = visitsAppService;
    	this._petsAppService = petsAppService;
    	this._vetsAppService = vetsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('VISITSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateVisitsOutput> create(@RequestBody @Valid CreateVisitsInput visits) {
		CreateVisitsOutput output=_visitsAppService.create(visits);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
		
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete visits ------------
	@PreAuthorize("hasAnyAuthority('VISITSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindVisitsByIdOutput output = _visitsAppService.findById(Integer.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a visits with a id=%s", id)));
	
    _visitsAppService.delete(Integer.valueOf(id));
    }
    
	
	// ------------ Update visits ------------
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateVisitsOutput> update(@PathVariable String id, @RequestBody @Valid UpdateVisitsInput visits) {
	    FindVisitsByIdOutput currentVisits = _visitsAppService.findById(Integer.valueOf(id));
		Optional.ofNullable(currentVisits).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Visits with id=%s not found.", id)));
		
		
		visits.setVersion(currentVisits.getVersion());
    	
	    return new ResponseEntity(_visitsAppService.update(Integer.valueOf(id),visits), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindVisitsByIdOutput> findById(@PathVariable String id) {
	
    	FindVisitsByIdOutput output = _visitsAppService.findById(Integer.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_visitsAppService.find(searchCriteria,Pageable));
	}
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(value = "/{id}/pets", method = RequestMethod.GET)
	public ResponseEntity<GetPetsOutput> getPets(@PathVariable String id) {
    	GetPetsOutput output= _visitsAppService.getPets(Integer.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(value = "/{id}/vets", method = RequestMethod.GET)
	public ResponseEntity<GetVetsOutput> getVets(@PathVariable String id) {
    	GetVetsOutput output= _visitsAppService.getVets(Integer.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}


}


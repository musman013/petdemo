package com.fastcode.demopet.restcontrollers;

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
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.domain.EmptyJsonResponse;
import com.fastcode.demopet.application.visits.VisitsAppService;
import com.fastcode.demopet.application.visits.dto.*;
import com.fastcode.demopet.application.invoices.InvoicesAppService;
import com.fastcode.demopet.application.invoices.dto.FindInvoicesByIdOutput;
import com.fastcode.demopet.application.pets.PetsAppService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demopet.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/visits")
public class VisitsController {

	@Autowired
	private VisitsAppService _visitsAppService;
    
    @Autowired
	private InvoicesAppService  _invoicesAppService;
    
    @Autowired
	private PetsAppService  _petsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public VisitsController(VisitsAppService visitsAppService, InvoicesAppService invoicesAppService, PetsAppService petsAppService,
	 LoggingHelper helper) {
		super();
		this._visitsAppService = visitsAppService;
    	this._invoicesAppService = invoicesAppService;
    	this._petsAppService = petsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('VISITSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateVisitsOutput> create(@RequestBody @Valid CreateVisitsInput visits) {
		CreateVisitsOutput output=_visitsAppService.create(visits);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete visits ------------
	@PreAuthorize("hasAnyAuthority('VISITSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindVisitsByIdOutput output = _visitsAppService.findById(Long.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a visits with a id=%s", id)));
	
    _visitsAppService.delete(Long.valueOf(id));
    }
    
	
	// ------------ Update visits ------------
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateVisitsOutput> update(@PathVariable String id, @RequestBody @Valid UpdateVisitsInput visits) {
	    FindVisitsByIdOutput currentVisits = _visitsAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentVisits).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Visits with id=%s not found.", id)));
		
		
		visits.setVersion(currentVisits.getVersion());
    	
	    return new ResponseEntity(_visitsAppService.update(Long.valueOf(id),visits), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindVisitsByIdOutput> findById(@PathVariable String id) {
	
    	FindVisitsByIdOutput output = _visitsAppService.findById(Long.valueOf(id));
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
	@RequestMapping(value = "/{id}/invoices", method = RequestMethod.GET)
	public ResponseEntity getInvoices(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_visitsAppService.parseInvoicesJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindInvoicesByIdOutput> output = _invoicesAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 
    @PreAuthorize("hasAnyAuthority('VISITSENTITY_READ')")
	@RequestMapping(value = "/{id}/pets", method = RequestMethod.GET)
	public ResponseEntity<GetPetsOutput> getPets(@PathVariable String id) {
    	GetPetsOutput output= _visitsAppService.getPets(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}


}


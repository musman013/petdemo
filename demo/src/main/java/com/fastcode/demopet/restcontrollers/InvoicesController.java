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
import com.fastcode.demopet.application.invoices.InvoicesAppService;
import com.fastcode.demopet.application.invoices.dto.*;
import com.fastcode.demopet.application.visits.VisitsAppService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demopet.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/invoices")
public class InvoicesController {

	@Autowired
	private InvoicesAppService _invoicesAppService;
    
    @Autowired
	private VisitsAppService  _visitsAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	
    
    public InvoicesController(InvoicesAppService invoicesAppService, VisitsAppService visitsAppService,
	 LoggingHelper helper) {
		super();
		this._invoicesAppService = invoicesAppService;
    	this._visitsAppService = visitsAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('INVOICESENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateInvoicesOutput> create(@RequestBody @Valid CreateInvoicesInput invoices) {
		CreateInvoicesOutput output=_invoicesAppService.create(invoices);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete invoices ------------
	@PreAuthorize("hasAnyAuthority('INVOICESENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindInvoicesByIdOutput output = _invoicesAppService.findById(Long.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a invoices with a id=%s", id)));
	
    _invoicesAppService.delete(Long.valueOf(id));
    }
    
	
	// ------------ Update invoices ------------
    @PreAuthorize("hasAnyAuthority('INVOICESENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateInvoicesOutput> update(@PathVariable String id, @RequestBody @Valid UpdateInvoicesInput invoices) {
	    FindInvoicesByIdOutput currentInvoices = _invoicesAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentInvoices).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Invoices with id=%s not found.", id)));
		
		
		invoices.setVersion(currentInvoices.getVersion());
    	
	    return new ResponseEntity(_invoicesAppService.update(Long.valueOf(id),invoices), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('INVOICESENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindInvoicesByIdOutput> findById(@PathVariable String id) {
	
    	FindInvoicesByIdOutput output = _invoicesAppService.findById(Long.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('INVOICESENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_invoicesAppService.find(searchCriteria,Pageable));
	}
    @PreAuthorize("hasAnyAuthority('INVOICESENTITY_READ')")
	@RequestMapping(value = "/{id}/visits", method = RequestMethod.GET)
	public ResponseEntity<GetVisitsOutput> getVisits(@PathVariable String id) {
    	GetVisitsOutput output= _invoicesAppService.getVisits(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}


}


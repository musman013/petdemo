package com.fastcode.demopet.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
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
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.domain.EmptyJsonResponse;
import com.fastcode.demopet.application.pets.PetsAppService;
import com.fastcode.demopet.application.pets.dto.*;
import com.fastcode.demopet.application.visits.VisitsAppService;
import com.fastcode.demopet.application.visits.dto.FindVisitsByIdOutput;
import com.fastcode.demopet.application.types.TypesAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.owners.OwnersAppService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demopet.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/pets")
public class PetsController {

	@Autowired
	private PetsAppService _petsAppService;
    
    @Autowired
	private VisitsAppService  _visitsAppService;

    @Autowired
    private UserAppService _userAppService;

	@Autowired
	private Environment env;
    
    public PetsController(PetsAppService petsAppService, VisitsAppService visitsAppService, UserAppService userAppService) {
		super();
		this._petsAppService = petsAppService;
    	this._visitsAppService = visitsAppService;
    	this._userAppService = userAppService;
	}

    @PreAuthorize("hasAnyAuthority('PETSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreatePetsOutput> create(@RequestBody @Valid CreatePetsInput pets, HttpServletRequest request) {
		
    	UserEntity user = _userAppService.getUser();
    	
    	if(pets.getOwnerId() == null)
    	{
    		pets.setOwnerId(user.getId());
    	}
    	
    	CreatePetsOutput output =_petsAppService.create(pets);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete pets ------------
	@PreAuthorize("hasAnyAuthority('PETSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindPetsByIdOutput output = _petsAppService.findById(Long.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a pets with a id=%s", id)));
	
    _petsAppService.delete(Long.valueOf(id));
    }
    
	
	// ------------ Update pets ------------
    @PreAuthorize("hasAnyAuthority('PETSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdatePetsOutput> update(@PathVariable String id, @RequestBody @Valid UpdatePetsInput pets) {
	    FindPetsByIdOutput currentPets = _petsAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentPets).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Pets with id=%s not found.", id)));
		
		pets.setVersion(currentPets.getVersion());
    	
	    return new ResponseEntity(_petsAppService.update(Long.valueOf(id),pets), HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindPetsByIdOutput> findById(@PathVariable String id) {
	
    	FindPetsByIdOutput output = _petsAppService.findById(Long.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
//    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);

		List<FindPetsByIdOutput> list = _petsAppService.find(searchCriteria,Pageable);
		UserEntity user = _userAppService.getUser();
		
		if(_userAppService.checkIsAdmin(user))
		{
			return ResponseEntity.ok(list);
		}
		else
		{
			return ResponseEntity.ok(_petsAppService.filterPets(list, user.getId()));
		}
		
	}
    
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/visits", method = RequestMethod.GET)
	public ResponseEntity getVisits(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_petsAppService.parseVisitsJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindVisitsByIdOutput> output = _visitsAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/types", method = RequestMethod.GET)
	public ResponseEntity<GetTypesOutput> getTypes(@PathVariable String id) {
    	GetTypesOutput output= _petsAppService.getTypes(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('PETSENTITY_READ')")
	@RequestMapping(value = "/{id}/owners", method = RequestMethod.GET)
	public ResponseEntity<GetOwnersOutput> getOwners(@PathVariable String id) {
    	GetOwnersOutput output= _petsAppService.getOwners(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}


}


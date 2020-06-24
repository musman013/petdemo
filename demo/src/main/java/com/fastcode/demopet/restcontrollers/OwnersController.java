package com.fastcode.demopet.restcontrollers;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.fastcode.demopet.application.authorization.user.IUserAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UserProfile;
import com.fastcode.demopet.application.owners.OwnersAppService;
import com.fastcode.demopet.application.owners.dto.*;
import com.fastcode.demopet.application.pets.PetsAppService;
import com.fastcode.demopet.application.pets.dto.FindPetsByIdOutput;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demopet.commons.logging.LoggingHelper;

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
	
	@Autowired
    private PasswordEncoder pEncoder;
	
	@Autowired
	private UserAppService _userAppService;
	
	
    
    public OwnersController(OwnersAppService ownersAppService, PetsAppService petsAppService,
	 LoggingHelper helper, UserAppService userAppService, PasswordEncoder passwordEncoder) {
		super();
		this._ownersAppService = ownersAppService;
    	this._petsAppService = petsAppService;
		this.logHelper = helper;
		this._userAppService = userAppService;
		this.pEncoder = passwordEncoder;
	}

//  @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/getProfile",method = RequestMethod.GET)
	public ResponseEntity<OwnerProfile> getProfile() {
		
		UserEntity user = _userAppService.getUser();
		FindOwnersByIdOutput currentowner = _ownersAppService.findById(user.getId());
		if(currentowner == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(_ownersAppService.getProfile(currentowner), HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
	public ResponseEntity<OwnerProfile> updateProfile(@RequestBody @Valid OwnerProfile ownerProfile) {
		UserEntity user = _userAppService.getUser();

		FindOwnersByIdOutput currentowner = _ownersAppService.findById(user.getId());
		if(currentowner == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		FindUserByNameOutput userOutput = _userAppService.findByEmailAddress(ownerProfile.getEmailAddress());
		if(userOutput != null && userOutput.getId() != user.getId())
		{
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}
		
		userOutput = _userAppService.findByEmailAddress(ownerProfile.getUserName());
		if(userOutput != null && userOutput.getId() !=user.getId())
		{
			logHelper.getLogger().error("There already exists a user with userName =%s", user.getUserName());
			throw new EntityExistsException(
					String.format("There already exists a user with userName =%s", user.getUserName()));
		}
		
		FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(user.getId());
		return new ResponseEntity(_ownersAppService.updateOwnerProfile(currentUser,ownerProfile), HttpStatus.OK);
	}
	
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateOwnersOutput> create(@RequestBody @Valid CreateOwnersInput owners) {
    	FindUserByNameOutput foundUser = _userAppService.findByUserName(owners.getUserName());

	     if (foundUser != null) {
	     	logHelper.getLogger().error("There already exists a user with a name=%s", owners.getUserName());
	        throw new EntityExistsException(
	        	String.format("There already exists a user with a name=%s", owners.getUserName()));
	    }
	    
	    foundUser = _userAppService.findByEmailAddress(owners.getEmailAddress());
		if (foundUser != null) {
			logHelper.getLogger().error("There already exists a user with a email=%s", owners.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", owners.getEmailAddress()));
		}
	    
		owners.setPassword(pEncoder.encode(owners.getPassword()));

		CreateOwnersOutput output=_ownersAppService.create(owners);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete owners ------------
	@PreAuthorize("hasAnyAuthority('OWNERSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindOwnersByIdOutput output = _ownersAppService.findById(Long.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a owners with a id=%s", id)));
	
    _ownersAppService.delete(Long.valueOf(id));
    
    }
    
	
	
	// ------------ Update owners ------------
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateOwnersOutput> update(@PathVariable String id, @RequestBody @Valid UpdateOwnersInput owners) {
	    FindOwnersByIdOutput currentOwners = _ownersAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentOwners).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Owners with id=%s not found.", id)));
		
		
		owners.setVersion(currentOwners.getVersion());
    	
	    return new ResponseEntity(_ownersAppService.update(Long.valueOf(id),owners), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('OWNERSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindOwnersByIdOutput> findById(@PathVariable String id) {
	
    	FindOwnersByIdOutput output = _ownersAppService.findById(Long.valueOf(id));
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


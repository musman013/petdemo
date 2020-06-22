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
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.application.authorization.user.dto.UserProfile;
import com.fastcode.demopet.application.owners.dto.FindOwnersByIdOutput;
import com.fastcode.demopet.application.vets.VetsAppService;
import com.fastcode.demopet.application.vets.dto.*;
import com.fastcode.demopet.application.vetspecialties.VetSpecialtiesAppService;
import com.fastcode.demopet.application.vetspecialties.dto.FindVetSpecialtiesByIdOutput;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.fastcode.demopet.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/vets")
public class VetsController {

	@Autowired
	private VetsAppService _vetsAppService;
    
    @Autowired
	private VetSpecialtiesAppService  _vetSpecialtiesAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	@Autowired
    private PasswordEncoder pEncoder;
	
	@Autowired
	private UserAppService _userAppService;
    
    public VetsController(VetsAppService vetsAppService, VetSpecialtiesAppService vetSpecialtiesAppService,
	 LoggingHelper helper, UserAppService userAppService, PasswordEncoder passwordEncoder
	 ) {
		super();
		this._vetsAppService = vetsAppService;
    	this._vetSpecialtiesAppService = vetSpecialtiesAppService;
		this.logHelper = helper;
		this._userAppService = userAppService;
		this.pEncoder = passwordEncoder;
	}
    
    @RequestMapping(value = "/getProfile",method = RequestMethod.GET)
   	public ResponseEntity<VetProfile> getProfile() {
   		UserEntity user = _userAppService.getUser();
   		FindVetsByIdOutput currentvet = _vetsAppService.findById(user.getId());
   		if(currentvet == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
   		return new ResponseEntity(_vetsAppService.getProfile(currentvet), HttpStatus.OK);
   	}

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
	public ResponseEntity<VetProfile> updateProfile(@RequestBody @Valid VetProfile vetProfile) {
		UserEntity user = _userAppService.getUser();

		FindVetsByIdOutput currentvet = _vetsAppService.findById(user.getId());
		if(currentvet == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		FindUserByNameOutput userOutput = _userAppService.findByEmailAddress(vetProfile.getEmailAddress());
		if(userOutput != null && userOutput.getId() !=user.getId())
		{
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}
		
		userOutput = _userAppService.findByEmailAddress(vetProfile.getUserName());
		if(userOutput != null && userOutput.getId() !=user.getId())
		{
			logHelper.getLogger().error("There already exists a user with userName =%s", user.getUserName());
			throw new EntityExistsException(
					String.format("There already exists a user with userName =%s", user.getUserName()));
		}
		
		FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(user.getId());
		return new ResponseEntity(_vetsAppService.updateVetProfile(currentUser,vetProfile), HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('VETSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateVetsOutput> create(@RequestBody @Valid CreateVetsInput vets) {
    	FindUserByNameOutput foundUser = _userAppService.findByUserName(vets.getUserName());

	     if (foundUser != null) {
	     	logHelper.getLogger().error("There already exists a user with a name=%s", vets.getUserName());
	        throw new EntityExistsException(
	        	String.format("There already exists a user with a name=%s", vets.getUserName()));
	    }
	    
	    foundUser = _userAppService.findByEmailAddress(vets.getEmailAddress());
		if (foundUser != null) {
			logHelper.getLogger().error("There already exists a user with a email=%s", vets.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", vets.getEmailAddress()));
		}
	    
		vets.setPassword(pEncoder.encode(vets.getPassword()));

		CreateVetsOutput output=_vetsAppService.create(vets);
		return new ResponseEntity(output, HttpStatus.OK);
	}
   
	// ------------ Delete vets ------------
	@PreAuthorize("hasAnyAuthority('VETSENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    FindVetsByIdOutput output = _vetsAppService.findById(Long.valueOf(id));
    Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a vets with a id=%s", id)));
	
    _vetsAppService.delete(Long.valueOf(id));
    }
    
	
	// ------------ Update vets ------------
    @PreAuthorize("hasAnyAuthority('VETSENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateVetsOutput> update(@PathVariable String id, @RequestBody @Valid UpdateVetsInput vets) {
	    FindVetsByIdOutput currentVets = _vetsAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentVets).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Vets with id=%s not found.", id)));
		
		
		vets.setVersion(currentVets.getVersion());
    	
	    return new ResponseEntity(_vetsAppService.update(Long.valueOf(id),vets), HttpStatus.OK);
	}
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindVetsByIdOutput> findById(@PathVariable String id) {
	
    	FindVetsByIdOutput output = _vetsAppService.findById(Long.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_vetsAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('VETSENTITY_READ')")
	@RequestMapping(value = "/{id}/vetSpecialties", method = RequestMethod.GET)
	public ResponseEntity getVetSpecialties(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_vetsAppService.parseVetSpecialtiesJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindVetSpecialtiesByIdOutput> output = _vetSpecialtiesAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 


}


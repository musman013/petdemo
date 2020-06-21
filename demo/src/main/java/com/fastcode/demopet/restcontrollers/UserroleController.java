package com.fastcode.demopet.restcontrollers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Optional;

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
import com.fastcode.demopet.domain.model.UserroleId;
import com.fastcode.demopet.security.JWTAppService;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.domain.EmptyJsonResponse;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.authorization.userrole.dto.*;
import com.fastcode.demopet.application.authorization.userrole.dto.GetRoleOutput;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.*;
import com.fastcode.demopet.commons.logging.LoggingHelper;

@RestController
@RequestMapping("/userrole")
public class UserroleController {

	@Autowired
	private UserroleAppService _userroleAppService;
    
    @Autowired
	private UserAppService  _userAppService;

	@Autowired
	private LoggingHelper logHelper;
	 
	@Autowired
 	private JWTAppService _jwtAppService;

	@Autowired
	private Environment env;
	
	 public UserroleController(UserroleAppService userroleAppService, UserAppService userAppService,
			 JWTAppService jwtAppService, LoggingHelper helper) {
		super();
		this._userroleAppService = userroleAppService;
		this._userAppService = userAppService;
		this._jwtAppService = jwtAppService;
		this.logHelper = helper;
	}

    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserroleOutput> create(@RequestBody @Valid CreateUserroleInput userrole) {
		
		CreateUserroleOutput output=_userroleAppService.create(userrole);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
	
		FindUserByIdOutput foundUser =_userAppService.findById(output.getUserId());
		_jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
	
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete userrole ------------
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
		
		UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
		Optional.ofNullable(userroleid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));
	
		FindUserroleByIdOutput output = _userroleAppService.findById(userroleid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a userrole with a id=%s", id)));
	
		 _userroleAppService.delete(userroleid);
	     
	  	FindUserByIdOutput foundUser =_userAppService.findById(output.getUserId());
		 _jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
    }
	
	// ------------ Update userrole ------------
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserroleOutput> update(@PathVariable String id, @RequestBody @Valid UpdateUserroleInput userrole) {
		
		UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
		Optional.ofNullable(userroleid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));
	
		FindUserroleByIdOutput currentUserrole = _userroleAppService.findById(userroleid);
		Optional.ofNullable(currentUserrole).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Userrole with id=%s not found.", id)));
	
		FindUserByIdOutput foundUser =_userAppService.findById(currentUserrole.getUserId());
		_jwtAppService.deleteAllUserTokens(foundUser.getUserName());  
		
		
		userrole.setVersion(currentUserrole.getVersion());
    	
		return new ResponseEntity(_userroleAppService.update(userroleid,userrole), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserroleByIdOutput> findById(@PathVariable String id) {
		UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
		Optional.ofNullable(userroleid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));
	
		FindUserroleByIdOutput output = _userroleAppService.findById(userroleid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userroleAppService.find(searchCriteria,Pageable));
	}
	
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<GetUserOutput> getUser(@PathVariable String id) {
		
		UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
		Optional.ofNullable(userroleid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));
		
		GetUserOutput output= _userroleAppService.getUser(userroleid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}/role", method = RequestMethod.GET)
	public ResponseEntity<GetRoleOutput> getRole(@PathVariable String id) {
		
		UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	    Optional.ofNullable(userroleid).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid id=%s", id)));
		
		GetRoleOutput output= _userroleAppService.getRole(userroleid);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

}


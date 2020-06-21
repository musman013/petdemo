package com.fastcode.demopet.restcontrollers;

import com.fastcode.demopet.application.authorization.userpermission.UserpermissionAppService;
import com.fastcode.demopet.application.authorization.userrole.UserroleAppService;
import com.fastcode.demopet.application.authorization.userrole.dto.FindUserroleByIdOutput;
import com.fastcode.demopet.application.authorization.userpermission.dto.FindUserpermissionByIdOutput;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.*;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.security.JWTAppService;
import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.commons.search.SearchUtils;
import com.fastcode.demopet.commons.application.OffsetBasedPageRequest;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.commons.domain.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserAppService _userAppService;
    
    @Autowired
	private UserpermissionAppService  _userpermissionAppService;
	
	@Autowired
	private UserroleAppService  _userroleAppService;
	
	@Autowired
    private PasswordEncoder pEncoder;
    
    @Autowired
 	private JWTAppService _jwtAppService;
 	

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	public UserController(UserAppService userAppService, UserpermissionAppService userpermissionAppService,
			UserroleAppService userroleAppService, PasswordEncoder pEncoder, JWTAppService jwtAppService, LoggingHelper logHelper) {
		super();
		this._userAppService = userAppService;
		this._userpermissionAppService = userpermissionAppService;
		this._userroleAppService = userroleAppService;
		this._jwtAppService = jwtAppService;
		this.pEncoder = pEncoder;
		this.logHelper = logHelper;
	}

	// CRUD Operations
	// ------------ Create a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserOutput> create(@RequestBody @Valid CreateUserInput user) {
		 FindUserByNameOutput foundUser = _userAppService.findByUserName(user.getUserName());

	     if (foundUser != null) {
	     	logHelper.getLogger().error("There already exists a user with a name=%s", user.getUserName());
	        throw new EntityExistsException(
	        	String.format("There already exists a user with a name=%s", user.getUserName()));
	    }
	    
	    foundUser = _userAppService.findByEmailAddress(user.getEmailAddress());
		if (foundUser != null) {
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}
	    
	    user.setPassword(pEncoder.encode(user.getPassword()));
	
	    CreateUserOutput output=_userAppService.create(user);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/getProfile",method = RequestMethod.GET)
	public ResponseEntity<UserProfile> getProfile() {
		UserEntity user = _userAppService.getUser();
		FindUserByIdOutput currentuser = _userAppService.findById(user.getId());
		return new ResponseEntity(_userAppService.getProfile(currentuser), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
	public ResponseEntity<UserProfile> UpdateProfile(@RequestBody @Valid UserProfile userProfile) {
		UserEntity user = _userAppService.getUser();

		FindUserByNameOutput userOutput = _userAppService.findByEmailAddress(userProfile.getEmailAddress());
		if(userOutput != null && userOutput.getId() !=user.getId())
		{
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}
		
		userOutput = _userAppService.findByEmailAddress(userProfile.getUserName());
		if(userOutput != null && userOutput.getId() !=user.getId())
		{
			logHelper.getLogger().error("There already exists a user with userName =%s", user.getUserName());
			throw new EntityExistsException(
					String.format("There already exists a user with userName =%s", user.getUserName()));
		}
		
		FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(user.getId());
		return new ResponseEntity(_userAppService.updateUserProfile(currentUser,userProfile), HttpStatus.OK);
	}

	// ------------ Delete a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    	FindUserByIdOutput existing = _userAppService.findById(Long.valueOf(id));
        Optional.ofNullable(existing).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a user with a id=%s",id)));
	
		_userAppService.delete(Long.valueOf(id));
	}
	
	// ------------ Update user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserOutput> update(@PathVariable String id, @RequestBody @Valid UpdateUserInput user) {
    	
    	FindUserWithAllFieldsByIdOutput currentUser = _userAppService.findWithAllFieldsById(Long.valueOf(id));
		Optional.ofNullable(currentUser).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. User with id=%s not found.",id)));
	
		FindUserByNameOutput foundUser = _userAppService.findByUserName(user.getUserName());

		if (foundUser != null && foundUser.getId() != user.getId()) {
			logHelper.getLogger().error("There already exists a user with a name=%s", user.getUserName());
			throw new EntityExistsException(
					String.format("There already exists a user with a name=%s", user.getUserName()));
		}

		foundUser = _userAppService.findByEmailAddress(user.getEmailAddress());
		if (foundUser != null && foundUser.getId() != user.getId()) {
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}
		
		user.setVersion(currentUser.getVersion());
		user.setPassword(currentUser.getPassword());
		if(currentUser.getIsActive() && !user.getIsActive()) { 
            _jwtAppService.deleteAllUserTokens(currentUser.getUserName()); 
        } 
		
    return new ResponseEntity(_userAppService.update(Long.valueOf(id),user), HttpStatus.OK);
	}
	// ------------ Retrieve a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserByIdOutput> findById(@PathVariable String id) {
	
    	FindUserByIdOutput output = _userAppService.findById(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userAppService.find(searchCriteria,Pageable));
	}
   
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{userid}/userpermission", method = RequestMethod.GET)
	public ResponseEntity getUserpermission(@PathVariable String userid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserpermissionJoinColumn(userid);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserpermissionByIdOutput> output = _userpermissionAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		return new ResponseEntity(output, HttpStatus.OK);
	}   
 
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{id}/userrole", method = RequestMethod.GET)
	public ResponseEntity getUserrole(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit),sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserroleJoinColumn(id);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
	
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserroleByIdOutput> output = _userroleAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
	
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   


}
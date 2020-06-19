package com.fastcode.demo.restcontrollers;

import com.fastcode.demo.application.authorization.permission.PermissionAppService;
import com.fastcode.demo.application.authorization.rolepermission.RolepermissionAppService;
import com.fastcode.demo.application.authorization.userpermission.UserpermissionAppService;
import com.fastcode.demo.application.authorization.userpermission.dto.FindUserpermissionByIdOutput;
import com.fastcode.demo.application.authorization.permission.dto.*;
import com.fastcode.demo.application.authorization.rolepermission.dto.FindRolepermissionByIdOutput;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.commons.search.SearchUtils;
import com.fastcode.demo.commons.application.OffsetBasedPageRequest;
import com.fastcode.demo.commons.logging.LoggingHelper;
import com.fastcode.demo.commons.domain.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionAppService _permissionAppService;
    
    @Autowired
	private RolepermissionAppService  _rolepermissionAppService;
    @Autowired
	private UserpermissionAppService  _userpermissionAppService;
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	public PermissionController(PermissionAppService appService,LoggingHelper helper,UserpermissionAppService userpermissionAppService,RolepermissionAppService rolepermissionAppService) {
		
		this._permissionAppService= appService;
		this.logHelper = helper;
		this._userpermissionAppService = userpermissionAppService;
		this._rolepermissionAppService = rolepermissionAppService;
	}

	// CRUD Operations

	// ------------ Create a permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreatePermissionOutput> create(@RequestBody @Valid CreatePermissionInput permission) {

		FindPermissionByNameOutput existing = _permissionAppService.findByPermissionName(permission.getName());
        
        if (existing != null) {
            logHelper.getLogger().error("There already exists a permission with name=%s", permission.getName());
            throw new EntityExistsException(
                    String.format("There already exists a permission with name=%s", permission.getName()));
        }
        
		CreatePermissionOutput output=_permissionAppService.create(permission);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    	FindPermissionByIdOutput output = _permissionAppService.findById(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a permission with a id=%s", id)));

   		_permissionAppService.delete(Long.valueOf(id));
    }
	
	// ------------ Update permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdatePermissionOutput> update(@PathVariable String id, @RequestBody @Valid UpdatePermissionInput permission) {
    
    	FindPermissionByIdOutput currentPermission = _permissionAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentPermission).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Permission with id=%s not found.", id)));
		
		FindPermissionByNameOutput foundPermission = _permissionAppService.findByPermissionName(permission.getName());

		if (foundPermission != null && foundPermission.getId() != permission.getId()) {
			logHelper.getLogger().error("There already exists a role with a name=%s", permission.getName());
			throw new EntityExistsException(
					String.format("There already exists a role with a name=%s", permission.getName()));
		}
		permission.setVersion(currentPermission.getVersion());
    	return new ResponseEntity(_permissionAppService.update(Long.valueOf(id),permission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindPermissionByIdOutput> findById(@PathVariable String id) {
    	
    	FindPermissionByIdOutput output = _permissionAppService.findById(Long.valueOf(id));
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_permissionAppService.find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{permissionid}/rolepermission", method = RequestMethod.GET)
	public ResponseEntity getRolepermission(@PathVariable String permissionid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails =_permissionAppService.parseRolepermissionJoinColumn(permissionid);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
		
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindRolepermissionByIdOutput> output = _rolepermissionAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{permissionid}/userpermission", method = RequestMethod.GET)
	public ResponseEntity getUserpermission(@PathVariable String permissionid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_permissionAppService.parseUserpermissionJoinColumn(permissionid);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid join column")));
		
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserpermissionByIdOutput> output = _userpermissionAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	} 
}

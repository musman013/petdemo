package com.fastcode.demo.restcontrollers;

import com.fastcode.demo.application.authorization.rolepermission.RolepermissionAppService;
import com.fastcode.demo.application.authorization.rolepermission.dto.FindRolepermissionByIdOutput;
import com.fastcode.demo.application.authorization.userrole.dto.FindUserroleByIdOutput;
import com.fastcode.demo.application.authorization.userrole.UserroleAppService;
import com.fastcode.demo.application.authorization.role.RoleAppService;
import com.fastcode.demo.application.authorization.role.dto.*;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.commons.search.SearchUtils;
import com.fastcode.demo.commons.application.OffsetBasedPageRequest;
import com.fastcode.demo.commons.domain.EmptyJsonResponse;
import com.fastcode.demo.commons.logging.LoggingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
	private UserroleAppService  _userroleAppService;
	@Autowired
	private RoleAppService _roleAppService;
    
    @Autowired
	private RolepermissionAppService  _rolepermissionAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
	
	public RoleController(RoleAppService appService,LoggingHelper helper,UserroleAppService userroleAppService,RolepermissionAppService rolepermissionAppService) {
		
		this._roleAppService= appService;
		this.logHelper = helper;
		this._userroleAppService = userroleAppService;
		this._rolepermissionAppService = rolepermissionAppService;
	}

    // CRUD Operations
    // ------------ Create a role ------------
    @PreAuthorize("hasAnyAuthority('ROLEENTITY_CREATE')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CreateRoleOutput> create(@RequestBody @Valid CreateRoleInput role) {

        FindRoleByNameOutput foundRole = _roleAppService.findByRoleName(role.getName());

        if (foundRole != null) {
            logHelper.getLogger().error("There already exists a role with name=%s", role.getName());
            throw new EntityExistsException(
                    String.format("There already exists a role with name=%s", role.getName()));
        }

        CreateRoleOutput output=_roleAppService.create(role);
		
		return new ResponseEntity(output, HttpStatus.OK);
    }

    // ------------ Delete role ------------
    @PreAuthorize("hasAnyAuthority('ROLEENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String id) {
	
    	FindRoleByIdOutput output = _roleAppService.findById(Long.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a role with a id=%s", id)));
		
   	 	_roleAppService.delete(Long.valueOf(id));
    }
    
    // ------------ Update role ------------
    @PreAuthorize("hasAnyAuthority('ROLEENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateRoleOutput> update(@PathVariable String id, @RequestBody @Valid UpdateRoleInput role) {
    	
    	FindRoleByIdOutput currentRole = _roleAppService.findById(Long.valueOf(id));
		Optional.ofNullable(currentRole).orElseThrow(() -> new EntityNotFoundException(String.format("Unable to update. Role with id=%s not found.", id)));
		
		FindRoleByNameOutput foundRole = _roleAppService.findByRoleName(role.getName());

		if (foundRole != null && foundRole.getId() != role.getId()) {
			logHelper.getLogger().error("There already exists a role with a name=%s", role.getName());
			throw new EntityExistsException(
					String.format("There already exists a role with a name=%s", role.getName()));
		}
		
		role.setVersion(currentRole.getVersion());
		
    	return new ResponseEntity(_roleAppService.update(Long.valueOf(id),role), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('ROLEENTITY_READ')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindRoleByIdOutput> findById(@PathVariable String id) {
	
    	FindRoleByIdOutput output = _roleAppService.findById(Long.valueOf(id));
    	Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('ROLEENTITY_READ')")
    @RequestMapping(method = RequestMethod.GET)
	public ResponseEntity find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		List<FindRoleByIdOutput> roles = _roleAppService.find(searchCriteria, Pageable);
 		return ResponseEntity.ok(roles);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLEENTITY_READ')")
	@RequestMapping(value = "/{roleid}/userrole", method = RequestMethod.GET)
	public ResponseEntity getUserrole(@PathVariable String roleid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_roleAppService.parseUserroleJoinColumn(roleid);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid Join Column")));
		
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserroleByIdOutput> output = _userroleAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	} 
	
	@PreAuthorize("hasAnyAuthority('ROLEENTITY_READ')")
    @RequestMapping(value = "/{roleid}/rolepermission", method = RequestMethod.GET)
	public ResponseEntity getRolepermission(@PathVariable String roleid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
	
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_roleAppService.parseRolepermissionJoinColumn(roleid);
		Optional.ofNullable(joinColDetails).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid Join Column")));
		
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindRolepermissionByIdOutput> output = _rolepermissionAppService.find(searchCriteria,pageable);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Not found")));
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   

}
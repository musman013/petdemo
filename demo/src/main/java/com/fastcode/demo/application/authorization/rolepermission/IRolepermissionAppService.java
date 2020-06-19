package com.fastcode.demo.application.authorization.rolepermission;

import java.util.List;

import com.fastcode.demo.domain.model.RolepermissionId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.authorization.rolepermission.dto.*;

@Service
public interface IRolepermissionAppService {

	CreateRolepermissionOutput create(CreateRolepermissionInput rolepermission);

    void delete(RolepermissionId rolepermissionId);

    UpdateRolepermissionOutput update(RolepermissionId rolepermissionId , UpdateRolepermissionInput input);

    FindRolepermissionByIdOutput findById(RolepermissionId rolepermissionId);

    List<FindRolepermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
	
	public RolepermissionId parseRolepermissionKey(String keysString);
	
    //Permission
    GetPermissionOutput getPermission(RolepermissionId rolepermissionId);
    
    //Role
    GetRoleOutput getRole(RolepermissionId rolepermissionId);
}

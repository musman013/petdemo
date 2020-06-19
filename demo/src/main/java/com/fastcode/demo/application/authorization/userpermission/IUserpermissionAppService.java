package com.fastcode.demo.application.authorization.userpermission;

import java.util.List;

import javax.validation.constraints.Positive;
import com.fastcode.demo.domain.model.UserpermissionId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fastcode.demo.commons.search.SearchCriteria;
import com.fastcode.demo.application.authorization.userpermission.dto.*;

@Service
public interface IUserpermissionAppService {

	CreateUserpermissionOutput create(CreateUserpermissionInput userpermission);
    
    void delete(UserpermissionId userpermissionId);

    UpdateUserpermissionOutput update(UserpermissionId userpermissionId , UpdateUserpermissionInput input);

    FindUserpermissionByIdOutput findById(UserpermissionId userpermissionId);

    List<FindUserpermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;
	
	public UserpermissionId parseUserpermissionKey(String keysString);
    
    //user
    GetUserOutput getUser(UserpermissionId userpermissionId);
    
    //Permission
    GetPermissionOutput getPermission(UserpermissionId userpermissionId);
}

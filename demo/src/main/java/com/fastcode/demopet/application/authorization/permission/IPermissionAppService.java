package com.fastcode.demopet.application.authorization.permission;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fastcode.demopet.commons.search.SearchCriteria;
import com.fastcode.demopet.application.authorization.permission.dto.*;

import java.util.List;

@Service
public interface IPermissionAppService {
    // CRUD Operations
    CreatePermissionOutput create(CreatePermissionInput input);

    void delete(Long pid);

    UpdatePermissionOutput update(Long permissionId, UpdatePermissionInput input);

    FindPermissionByIdOutput findById(Long pid);

    List<FindPermissionByIdOutput> find(SearchCriteria search, Pageable pageable) throws Exception;

}

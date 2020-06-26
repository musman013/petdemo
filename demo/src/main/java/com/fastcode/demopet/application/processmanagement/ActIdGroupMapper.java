package com.fastcode.demopet.application.processmanagement;

import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.processmanagement.groups.ActIdGroupEntity;
import org.springframework.stereotype.Component;

@Component
public class ActIdGroupMapper {
public ActIdGroupEntity createRolesEntityToActIdGroupEntity(RoleEntity role) {

    if ( role == null ) {
        return null;
    }

    ActIdGroupEntity actIdGroup = new ActIdGroupEntity();
    actIdGroup.setId(role.getName());
    actIdGroup.setName(role.getName());
    actIdGroup.setRev(0L);
    actIdGroup.setType(null);

    return actIdGroup;
    }
}

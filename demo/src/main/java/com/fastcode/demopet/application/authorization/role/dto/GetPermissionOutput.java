package com.fastcode.demopet.application.authorization.role.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetPermissionOutput {

    private Long id;
    private String displayName;
    private String name;
    private Long roleId;
    private String roleDescriptiveField;

}

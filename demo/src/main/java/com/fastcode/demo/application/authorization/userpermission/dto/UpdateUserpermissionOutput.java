package com.fastcode.demo.application.authorization.userpermission.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserpermissionOutput {

    private Long permissionId;
    private Long userId;
    private String userDescriptiveField;
  	private String permissionDescriptiveField;
    private Boolean revoked;
    
}
package com.fastcode.demopet.application.authorization.userpermission.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserpermissionOutput {

  	private Long permissionId;
    private Long userId;
    private String userDescriptiveField;
  	private String permissionDescriptiveField;
  	private Boolean revoked;
    
}

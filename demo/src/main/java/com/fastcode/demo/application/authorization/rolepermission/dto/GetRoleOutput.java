package com.fastcode.demo.application.authorization.rolepermission.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetRoleOutput {

  	private String displayName;
  	private Long id;
  	private String name;

  	private Long rolepermissionPermissionId;
  	private Long rolepermissionRoleId;
 
}

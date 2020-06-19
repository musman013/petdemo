package com.fastcode.demo.application.authorization.role.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindRoleByIdOutput {

    private Long id;
    private String displayName;
    private String name;
    private Long version;
  
}
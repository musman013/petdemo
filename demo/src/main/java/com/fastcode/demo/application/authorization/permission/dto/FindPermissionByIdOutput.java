package com.fastcode.demo.application.authorization.permission.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindPermissionByIdOutput {

    private Long id;
    private String displayName;
    private String name;
    private Long version;
  
}


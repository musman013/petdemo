package com.fastcode.demo.application.authorization.userrole.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindUserroleByIdOutput {

    private Long roleId;
    private Long userId;
    private String userDescriptiveField;
    private String roleDescriptiveField;
    private Long version;
  
}

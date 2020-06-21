package com.fastcode.demopet.application.authorization.userrole.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserroleOutput {

    private Long roleId;
    private Long userId;
    private String userDescriptiveField;
	private String roleDescriptiveField;
   
}

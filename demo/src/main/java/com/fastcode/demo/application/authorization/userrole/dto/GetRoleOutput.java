package com.fastcode.demo.application.authorization.userrole.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetRoleOutput {
    
    private String displayName;
    private Long id;
    private String name;
    private Long userroleRoleId;
    private Long userroleUserId;
 
}

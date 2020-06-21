package com.fastcode.demopet.application.authorization.userpermission.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserpermissionInput {

    @NotNull(message = "permissionId Should not be null")
    private Long permissionId;
  
    @NotNull(message = "user Id Should not be null")
    private Long userId;
    private Boolean revoked;
    
    private Long version;
  
}

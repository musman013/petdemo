package com.fastcode.demo.application.authorization.userrole.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserroleInput {

  	@NotNull(message = "roleId Should not be null")
  	private Long roleId;
  
  	@NotNull(message = "user Id Should not be null")
  	private Long userId;
  	
  	private Long version;
  
}

package com.fastcode.demopet.application.authorization.role.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateRoleInput {
	
	@NotNull(message = "Id Should not be null")
	private Long id;
    
    @NotNull(message = "Display Name Should not be null")
    @Length(max = 128, message = "Display Name must be less than 128 characters")
    private String displayName;
    
    @NotNull(message = "Name Should not be null")
    @Length(max = 128, message = "Name must be less than 128 characters")
    private String name;

    private Long version;
  
}

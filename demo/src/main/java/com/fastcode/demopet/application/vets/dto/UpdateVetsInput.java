package com.fastcode.demopet.application.vets.dto;

import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVetsInput extends UpdateUserInput{

//  	private Integer id;
  	private Long version;
  
}

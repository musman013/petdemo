package com.fastcode.demopet.application.vets.dto;

import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindVetsByIdOutput extends FindUserByNameOutput{

//  	private Integer id;
	private Long version;
 
}

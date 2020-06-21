package com.fastcode.demopet.application.owners.dto;

import java.util.Date;

import com.fastcode.demopet.application.authorization.user.dto.FindUserByIdOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindOwnersByIdOutput extends FindUserByIdOutput {

  	private String address;
  	private String city;
//  	private Integer id;

	private Long version;
 
}

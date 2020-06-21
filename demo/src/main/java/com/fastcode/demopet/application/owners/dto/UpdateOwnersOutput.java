package com.fastcode.demopet.application.owners.dto;

import java.util.Date;

import com.fastcode.demopet.application.authorization.user.dto.UpdateUserOutput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateOwnersOutput extends UpdateUserOutput {

  	private String address;
  	private String city;
//  	private Integer id;

}
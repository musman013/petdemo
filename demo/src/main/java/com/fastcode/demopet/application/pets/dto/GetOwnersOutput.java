package com.fastcode.demopet.application.pets.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetOwnersOutput {

  	private String address;
  	private String city;
  	private String firstName;
  	private Long id;
  	private String lastName;
  	private String telephone;

  	private Long petsId;

}

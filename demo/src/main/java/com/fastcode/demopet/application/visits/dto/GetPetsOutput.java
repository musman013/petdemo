package com.fastcode.demopet.application.visits.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetPetsOutput {

  	private Date birthDate;
  	private Long id;
  	private String name;

  	private Long visitsId;

}

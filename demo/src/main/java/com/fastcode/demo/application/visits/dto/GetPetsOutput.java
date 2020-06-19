package com.fastcode.demo.application.visits.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetPetsOutput {

  	private Date birthDate;
  	private Integer id;
  	private String name;

  	private Integer visitsId;

}

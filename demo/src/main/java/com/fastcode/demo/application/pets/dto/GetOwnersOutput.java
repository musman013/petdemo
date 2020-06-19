package com.fastcode.demo.application.pets.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetOwnersOutput {

  	private String address;
  	private String city;
  	private Integer id;

  	private Integer petsId;

}

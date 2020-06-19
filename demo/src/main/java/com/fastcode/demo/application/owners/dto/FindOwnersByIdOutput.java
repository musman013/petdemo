package com.fastcode.demo.application.owners.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindOwnersByIdOutput {

  	private String address;
  	private String city;
  	private Integer id;

	private Long version;
 
}

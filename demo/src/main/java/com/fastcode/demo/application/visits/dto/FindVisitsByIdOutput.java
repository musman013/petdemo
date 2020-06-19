package com.fastcode.demo.application.visits.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindVisitsByIdOutput {

  	private String description;
  	private Integer id;
  	private Date visitDate;
  	private Integer petId;
  	private String petsDescriptiveField;
  	private Integer vetId;
  	private Integer vetsDescriptiveField;

	private Long version;
 
}

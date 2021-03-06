package com.fastcode.demopet.application.visits.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVisitsOutput {

  	private String description;
  	private Long id;
  	private Date visitDate;
  	private Status status;
    private String visitNotes;
  	private Long petId;
	private String petsDescriptiveField;
	private Long vetId;
  	private String vetsDescriptiveField;

}
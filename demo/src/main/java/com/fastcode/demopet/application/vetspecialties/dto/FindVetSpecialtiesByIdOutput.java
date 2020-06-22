package com.fastcode.demopet.application.vetspecialties.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindVetSpecialtiesByIdOutput {

  	private Long specialtyId;
  	private Long vetId;
  	private String specialtiesDescriptiveField;
  	private String vetsDescriptiveField;
	private Long version;
 
}

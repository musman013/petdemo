package com.fastcode.demo.application.vetspecialties.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindVetSpecialtiesByIdOutput {

  	private Integer specialtyId;
  	private Integer vetId;
  	private String specialtiesDescriptiveField;
  	private Integer vetsDescriptiveField;

	private Long version;
 
}

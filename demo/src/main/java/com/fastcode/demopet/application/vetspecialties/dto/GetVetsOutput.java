package com.fastcode.demopet.application.vetspecialties.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetVetsOutput {

  	private String firstName;
  	private Long id;
  	private String lastName;

  	private Long vetSpecialtiesSpecialtyId;
  	private Long vetSpecialtiesVetId;

}

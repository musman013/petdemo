package com.fastcode.demopet.application.vetspecialties.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetSpecialtiesOutput {

  	private Long id;
  	private String name;

  	private Long vetSpecialtiesSpecialtyId;
  	private Long vetSpecialtiesVetId;

}

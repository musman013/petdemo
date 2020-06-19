package com.fastcode.demo.application.vetspecialties.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetSpecialtiesOutput {

  	private Integer id;
  	private String name;

  	private Integer vetSpecialtiesSpecialtyId;
  	private Integer vetSpecialtiesVetId;

}

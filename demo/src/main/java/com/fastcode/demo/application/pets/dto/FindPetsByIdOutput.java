package com.fastcode.demo.application.pets.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindPetsByIdOutput {

  	private Date birthDate;
  	private Integer id;
  	private String name;
  	private Integer typeId;
  	private String typesDescriptiveField;
  	private Integer ownerId;
  	private Integer ownersDescriptiveField;

	private Long version;
 
}

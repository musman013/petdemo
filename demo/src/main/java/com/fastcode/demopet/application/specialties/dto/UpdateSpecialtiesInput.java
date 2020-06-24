package com.fastcode.demopet.application.specialties.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateSpecialtiesInput {

  	@NotNull(message = "id Should not be null")
  	private Long id;
  	@Length(max = 80, message = "name must be less than 80 characters")
  	private String name;
  	private Long version;
  
}

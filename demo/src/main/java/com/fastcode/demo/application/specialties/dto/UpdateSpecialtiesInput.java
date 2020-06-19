package com.fastcode.demo.application.specialties.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateSpecialtiesInput {

  	@NotNull(message = "id Should not be null")
  	private Integer id;
  	@Length(max = 80, message = "name must be less than 80 characters")
  	private String name;
  	private Long version;
  
}

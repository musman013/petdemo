package com.fastcode.demopet.application.pets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdatePetsInput {

  	private Date birthDate;
  	@NotNull(message = "id Should not be null")
  	private Long id;
  	@Length(max = 30, message = "name must be less than 30 characters")
  	private String name;
  	private Long typeId;
  	private Long ownerId;
  	private Long version;
  
}

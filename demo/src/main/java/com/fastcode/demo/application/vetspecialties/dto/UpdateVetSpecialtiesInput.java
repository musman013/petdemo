package com.fastcode.demo.application.vetspecialties.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVetSpecialtiesInput {

  	@NotNull(message = "specialtyId Should not be null")
  	private Integer specialtyId;
  	@NotNull(message = "vetId Should not be null")
  	private Integer vetId;
  	private Long version;
  
}

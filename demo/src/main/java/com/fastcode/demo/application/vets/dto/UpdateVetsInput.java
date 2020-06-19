package com.fastcode.demo.application.vets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVetsInput {

  	@NotNull(message = "id Should not be null")
  	private Integer id;
  	private Long version;
  
}

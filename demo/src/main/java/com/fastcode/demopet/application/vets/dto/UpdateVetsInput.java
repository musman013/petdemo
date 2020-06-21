package com.fastcode.demopet.application.vets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVetsInput extends UpdateUserInput{

  	@NotNull(message = "id Should not be null")
//  	private Integer id;
  	private Long version;
  
}

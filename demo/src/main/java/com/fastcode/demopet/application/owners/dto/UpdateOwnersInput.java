package com.fastcode.demopet.application.owners.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.application.authorization.user.dto.UpdateUserInput;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateOwnersInput extends UpdateUserInput{

  	private String address;
  	@Length(max = 80, message = "city must be less than 80 characters")
  	private String city;
  	@NotNull(message = "id Should not be null")
//  	private Integer id;
  	private Long version;
  
}

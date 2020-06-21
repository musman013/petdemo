package com.fastcode.demopet.application.specialties.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateSpecialtiesInput {

  @Length(max = 80, message = "name must be less than 80 characters")
  private String name;
  
 
}

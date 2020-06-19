package com.fastcode.demo.application.types.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateTypesInput {

  @Length(max = 80, message = "name must be less than 80 characters")
  private String name;
  
 
}

package com.fastcode.demo.application.owners.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateOwnersInput {

  private String address;
  
  @Length(max = 80, message = "city must be less than 80 characters")
  private String city;
  
 
}

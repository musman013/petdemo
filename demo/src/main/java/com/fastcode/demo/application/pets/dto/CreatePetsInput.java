package com.fastcode.demo.application.pets.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePetsInput {

  private Date birthDate;
  
  @Length(max = 30, message = "name must be less than 30 characters")
  private String name;
  
  private Integer typeId;
  private Integer ownerId;
 
}

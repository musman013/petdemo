package com.fastcode.demo.application.visits.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateVisitsInput {

  private String description;
  
  private Date visitDate;
  
  private Integer petId;
  private Integer vetId;
 
}

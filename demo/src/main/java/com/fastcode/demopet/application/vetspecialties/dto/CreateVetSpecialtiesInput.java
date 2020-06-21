package com.fastcode.demopet.application.vetspecialties.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateVetSpecialtiesInput {

  @NotNull(message = "specialtyId Should not be null")
  private Long specialtyId;
  
  @NotNull(message = "vetId Should not be null")
  private Long vetId;
  
 
}

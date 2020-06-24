package com.fastcode.demopet.application.visits.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateVisitsInput {

  private String description;
  private Status status;
  private String visitNotes;
  @NotNull
  private Date visitDate;
  private Long petId;
  private Long vetId;
 
}

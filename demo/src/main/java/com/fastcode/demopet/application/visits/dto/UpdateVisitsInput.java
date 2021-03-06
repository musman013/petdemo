package com.fastcode.demopet.application.visits.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVisitsInput {

  	private String description;
  	@NotNull(message = "id Should not be null")
  	private Long id;
  	private Date visitDate;
  	private Status status;
    private String visitNotes;
  	private Long petId;
  	private Long vetId;
  	private Long version;
  
}

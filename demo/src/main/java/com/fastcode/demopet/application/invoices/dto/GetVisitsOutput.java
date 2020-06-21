package com.fastcode.demopet.application.invoices.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetVisitsOutput {

  	private String description;
  	private Long id;
  	private Date visitDate;

  	private Long invoicesId;

}

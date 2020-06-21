package com.fastcode.demopet.application.invoices.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateInvoicesInput {

  private Long amount;
  
  private Long visitId;
 
}

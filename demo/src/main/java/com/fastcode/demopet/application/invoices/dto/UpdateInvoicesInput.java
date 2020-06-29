package com.fastcode.demopet.application.invoices.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.fastcode.demopet.domain.model.InvoicesEntity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateInvoicesInput {

  	private Long amount;
  	@NotNull(message = "id Should not be null")
  	private Long id;
  	private Long visitId;
  	private Long version;
  	private InvoiceStatus status;
    private String processInstanceId;
  
}

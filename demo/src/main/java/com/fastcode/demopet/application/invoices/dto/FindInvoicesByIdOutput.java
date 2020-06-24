package com.fastcode.demopet.application.invoices.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindInvoicesByIdOutput {

  	private Long amount;
  	private Long id;
  	private Long visitId;
  	private Long visitsDescriptiveField;
	private Long version;
	private InvoiceStatus status;
 
}

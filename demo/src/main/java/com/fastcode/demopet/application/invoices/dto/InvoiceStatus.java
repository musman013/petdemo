package com.fastcode.demopet.application.invoices.dto;

public enum InvoiceStatus {

	PAID("paid"), UNPAID("unpaid");

    private final String status;       

    private InvoiceStatus (String s) {
        status = s;
    }
    
    public String getInvoiceStatus() {
        return status;
    }
	
}

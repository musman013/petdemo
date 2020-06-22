package com.fastcode.demopet.application.visits.dto;

public enum Status{
	
	CREATED ("created"), CONFIRMED("confirmed"), CANCELLED("cancelled"), COMPLETED("completed");

    private final String status;       

    private Status (String s) {
        status = s;
    }
    
    public String getStatus() {
        return status;
    }

}

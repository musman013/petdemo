package com.fastcode.demopet.application.pets.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreatePetsOutput {

    private Date birthDate;
    private Long id;
    private String name;
	private Long typeId;
	private String typesDescriptiveField;
	private Long ownerId;
	private String ownersDescriptiveField;

}

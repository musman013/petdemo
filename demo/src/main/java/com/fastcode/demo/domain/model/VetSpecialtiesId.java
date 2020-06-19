package com.fastcode.demo.domain.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class VetSpecialtiesId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Integer specialtyId;
    private Integer vetId;
    
    public VetSpecialtiesId(Integer specialtyId,Integer vetId) {
  		this.specialtyId =specialtyId;
  		this.vetId =vetId;
    }
    
}
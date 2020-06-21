package com.fastcode.demopet.domain.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class VetSpecialtiesId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long specialtyId;
    private Long vetId;
    
    public VetSpecialtiesId(Long specialtyId,Long vetId) {
  		this.specialtyId =specialtyId;
  		this.vetId =vetId;
    }
    
}
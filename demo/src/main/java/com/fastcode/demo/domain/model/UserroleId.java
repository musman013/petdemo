package com.fastcode.demo.domain.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
public class UserroleId implements Serializable {

	private static final long serialVersionUID = 1L;
    private Long roleId;
    private Long userId;

    public UserroleId(Long roleId,Long userId) {
  		this.roleId =roleId;
   		this.userId =userId;
    }
    
}
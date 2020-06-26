package com.fastcode.demopet.domain.processmanagement.privileges;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "act_id_priv")
@Getter @Setter
@NoArgsConstructor
public class ActIdPrivEntity  implements Serializable {

	@Id
	@Column(name = "id_", nullable = false, length =64)
	private String id;

	@Basic
	@Column(name = "name_", nullable = false, length =255)
	private String name;

//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (!(o instanceof ActIdPrivEntity)) return false;
//		ActIdPrivEntity actidpriv = (ActIdPrivEntity) o;
//		return id != null && id.equals(actidpriv.id);
//	}
//	
}






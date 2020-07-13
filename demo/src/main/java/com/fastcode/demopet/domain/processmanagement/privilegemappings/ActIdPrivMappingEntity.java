package com.fastcode.demopet.domain.processmanagement.privilegemappings;

import java.io.Serializable;

import javax.persistence.*;

import com.fastcode.demopet.domain.processmanagement.privileges.ActIdPrivEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "act_id_priv_mapping", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class ActIdPrivMappingEntity  implements Serializable {

	@ManyToOne
	@JoinColumn(name = "priv_id_")
	private ActIdPrivEntity actIdPriv;


	@Basic
	@Column(name = "group_id_", nullable = true, length =255)
	private String groupId;


	@Id
	@Column(name = "id_", nullable = false, length =64)
	private String id;


	@Basic
	@Column(name = "user_id_", nullable = true, length =255)
	private String userId;

//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (!(o instanceof ActIdPrivMappingEntity)) return false;
//		ActIdPrivMappingEntity actidprivmapping = (ActIdPrivMappingEntity) o;
//		return id != null && id.equals(actidprivmapping.id);
//	}
}






package com.fastcode.demopet.domain.processmanagement.groups;

import java.io.Serializable;

import javax.persistence.*;

import com.fastcode.demopet.domain.processmanagement.memberships.ActIdMembershipEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "act_id_group", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class ActIdGroupEntity  implements Serializable {

	@OneToMany(mappedBy = "actIdGroup", cascade = CascadeType.ALL, orphanRemoval = true) 
	private Set<ActIdMembershipEntity> actIdUser = new HashSet<>(); 

    @Id
    @Column(name = "id_", nullable = false, length =64)
    private String id;

    @Basic
    @Column(name = "name_", nullable = true, length =255)
    private String name;

    @Basic
    @Column(name = "rev_", nullable = true)
    private Long rev;

    @Basic
    @Column(name = "type_", nullable = true, length =255)
    private String type;

//    @Override
//    public boolean equals(Object o) {
//    	if (this == o) return true;
//    	if (!(o instanceof ActIdGroupEntity)) return false;
//    	ActIdGroupEntity actidgroup = (ActIdGroupEntity) o;
//    	return id != null && id.equals(actidgroup.id);
//    }
}






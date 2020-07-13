package com.fastcode.demopet.domain.processmanagement.users;

import java.io.Serializable;

import javax.persistence.*;

import com.fastcode.demopet.domain.processmanagement.memberships.ActIdMembershipEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "act_id_user", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class ActIdUserEntity  implements Serializable {

	@Basic
    @Column(name = "display_name_", nullable = true, length =255)
	private String displayName;
	

	@Basic
    @Column(name = "email_", nullable = true, length =255)
	private String email;
	
	@Basic
    @Column(name = "first_", nullable = true, length =255)
	private String first;
	
	@Id
    @Column(name = "id_", nullable = false, length =64)
	private String id;
	
	@Basic
    @Column(name = "last_", nullable = true, length =255)
	private String last;
	
	@Basic
    @Column(name = "picture_id_", nullable = true, length =64)
	private String pictureId;
	
	@Basic
    @Column(name = "pwd_", nullable = true, length =255)
	private String pwd;
	
	@Basic
    @Column(name = "rev_", nullable = true)
	private Long rev;
	
//	@Basic
//	@Column(name = "tenant_id_", nullable = true, length =255)
//	private String tenantId;

	@OneToMany(mappedBy = "actIdUser", cascade = CascadeType.ALL, orphanRemoval = true) 
	private Set<ActIdMembershipEntity> actIdGroup = new HashSet<>();
   
//  
//
//    @Override
//    public boolean equals(Object o) {
//    	if (this == o) return true;
//    	if (!(o instanceof ActIdUserEntity)) return false;
//    	ActIdUserEntity actiduser = (ActIdUserEntity) o;
//    	return id != null && id.equals(actiduser.id);
//    }

}






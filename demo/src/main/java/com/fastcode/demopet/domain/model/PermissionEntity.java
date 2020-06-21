package com.fastcode.demopet.domain.model;

import com.fastcode.demopet.domain.model.RoleEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Permission", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class PermissionEntity extends AbstractEntity {
    
    @Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Column(name = "Name", nullable = false, length = 128,unique = true)
    private String name;
    
    @Basic
    @Column(name = "DisplayName", nullable = false, length = 128)
    private String displayName;
    
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL) 
    private Set<RolepermissionEntity> rolepermissionSet = new HashSet<RolepermissionEntity>();
    
    public void addRolepermission(RolepermissionEntity rolepermission) {
		rolepermissionSet.add(rolepermission);
		rolepermission.setPermission(this);
	}

	public void removeRolepermission(RolepermissionEntity rolepermission) {
		rolepermissionSet.remove(rolepermission);
		rolepermission.setPermission(null);
	} 
    
    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL) 
    private Set<UserpermissionEntity> userpermissionSet = new HashSet<UserpermissionEntity>(); 

	public void addUserpermission(UserpermissionEntity userpermission) {
		userpermissionSet.add(userpermission);
		userpermission.setPermission(this);
	}

	public void removeUserpermission(UserpermissionEntity userpermission) {
		userpermissionSet.remove(userpermission);
		userpermission.setPermission(null);
	}
	
	public PermissionEntity(String name, String displayName) {
    	this.name = name;
    	this.displayName = displayName;
    }
}

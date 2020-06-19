package com.fastcode.demo.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Role", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class RoleEntity extends AbstractEntity {

	@Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Column(name = "DisplayName", nullable = false, length = 128)
    private String displayName;
    
    @Basic
    @Column(name = "Name", nullable = false, length = 128)
    private String name;
    

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<RolepermissionEntity> rolepermissionSet = new HashSet<RolepermissionEntity>(); 
    
    public void addRolepermission(RolepermissionEntity rolepermission) {
        rolepermissionSet.add(rolepermission);
        rolepermission.setRole(this);
    }
 
    public void removeRolepermission(RolepermissionEntity rolepermission) {
    	rolepermissionSet.remove(rolepermission);
        rolepermission.setRole(null);
    }
  
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true) 
    private Set<UserroleEntity> userroleSet = new HashSet<UserroleEntity>();
    
    public void addUserrole(UserroleEntity userrole) {
        userroleSet.add(userrole);
        userrole.setRole(this);
    }
 
    public void removeUserrole(UserroleEntity userrole) {
    	userroleSet.remove(userrole);
        userrole.setRole(null);
    } 

}

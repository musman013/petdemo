package com.fastcode.demopet.domain.model;

import javax.persistence.*;

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
    

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
    private Set<RolepermissionEntity> rolepermissionSet = new HashSet<RolepermissionEntity>(); 
    
    public void addRolepermission(RolepermissionEntity rolepermission) {
        rolepermissionSet.add(rolepermission);
        rolepermission.setRole(this);
    }
 
    public void removeRolepermission(RolepermissionEntity rolepermission) {
    	rolepermissionSet.remove(rolepermission);
        rolepermission.setRole(null);
    }
  
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
    private Set<UserroleEntity> userroleSet = new HashSet<UserroleEntity>();
    
    public void addUserrole(UserroleEntity userrole) {
        userroleSet.add(userrole);
        userrole.setRole(this);
    }
 
    public void removeUserrole(UserroleEntity userrole) {
    	userroleSet.remove(userrole);
        userrole.setRole(null);
    } 
    
    
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
  	private Set<ReportroleEntity> reportroleSet = new HashSet<ReportroleEntity>(); 
  	
  	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL) 
  	private Set<DashboardroleEntity> dashboardroleSet = new HashSet<DashboardroleEntity>(); 
    
    public void addDashboardrole(DashboardroleEntity dashboardrole) {
        dashboardroleSet.add(dashboardrole);
        dashboardrole.setRole(this);
    }
 
    public void removeDashboardrole(DashboardroleEntity dashboardrole) {
    	dashboardroleSet.remove(dashboardrole);
        dashboardrole.setRole(null);
    }
    
    public void addReportrole(ReportroleEntity reportrole) {
        reportroleSet.add(reportrole);
        reportrole.setRole(this);
    }
 
    public void removeReportrole(ReportroleEntity reportrole) {
    	reportroleSet.remove(reportrole);
        reportrole.setRole(null);
    }

}

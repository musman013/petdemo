package com.fastcode.demopet.domain.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "dashboard")
public class DashboardEntity extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Basic
	@Column(name = "isPublished", nullable = false)
	private Boolean isPublished;

	@Basic
	@Column(name = "isShareable", nullable = false)
	private Boolean isShareable;

	@ManyToOne
	@JoinColumn(name = "ownerId")
	private UserEntity user;

	@OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL) 
	private Set<DashboardversionEntity> dashboardversionSet = new HashSet<DashboardversionEntity>(); 

	@OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL) 
	private Set<DashboarduserEntity> dashboarduserSet = new HashSet<DashboarduserEntity>(); 

	@OneToMany(mappedBy = "dashboard", cascade = CascadeType.ALL) 
	private Set<DashboardroleEntity> dashboardroleSet = new HashSet<DashboardroleEntity>(); 

	public void addDashboardversion(DashboardversionEntity dashboardversion) {
		dashboardversionSet.add(dashboardversion);
		dashboardversion.setDashboard(this);
	}
	
	public void removeDashboardversion(DashboardversionEntity dashboardversion) {
		dashboardversionSet.remove(dashboardversion);
		dashboardversion.setDashboard(null);
	}

	public void addDashboardrole(DashboardroleEntity dashboardrole) {
		dashboardroleSet.add(dashboardrole);
		dashboardrole.setDashboard(this);
	}

	public void removeDashboardrole(DashboardroleEntity dashboardrole) {
		dashboardroleSet.remove(dashboardrole);
		dashboardrole.setDashboard(null);
	}

	public void addDashboarduser(DashboarduserEntity dashboarduser) {
		dashboarduserSet.add(dashboarduser);
		dashboarduser.setDashboard(this);
	}

	public void removeDashboarduser(DashboarduserEntity dashboarduser) {
		dashboarduserSet.remove(dashboarduser);
		dashboarduser.setDashboard(null);
	}
	
	@PreRemove
	private void dismissParent() {
		//SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
		if(this.user != null) {
			this.user.removeDashboard(this);
			this.user = null;

		}
	}


}






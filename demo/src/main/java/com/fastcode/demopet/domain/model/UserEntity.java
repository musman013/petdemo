package com.fastcode.demopet.domain.model;

import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fUser", schema = "sample")
@Getter @Setter
@NoArgsConstructor
public class  UserEntity extends AbstractEntity {

	@Id
    @Column(name = "Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    @Column(name = "FirstName", nullable = false, length = 32)
    @NotNull
    @Length(max = 32, message = "The field must be less than 32 characters")
    private String firstName;
    
    @Basic
    @Column(name = "LastName", nullable = false, length = 32)
    @NotNull
    @Length(max = 32, message = "The field must be less than 32 characters")
    private String lastName;
    
    @Basic
    @Column(name = "EmailAddress", nullable = false, length = 256)
    @Email
    @NotNull
    @Length(max = 256, message = "The field must be less than 256 characters")
    private String emailAddress;
    
    @Basic
    @Column(name = "UserName", nullable = false, length = 32, unique = true)
    @NotNull
    @Length(max = 32, message = "The field must be less than 32 characters")
    private String userName;
    
    @Basic
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;
    
    @Basic
    @Column(name = "Password", nullable = false, length = 128)
    private String password;
    
    @Basic
    @Column(name = "PasswordResetCode", nullable = true, length = 328)
    private String passwordResetCode;
    
    @Basic
    @Column(name = "ShouldChangePasswordOnNextLogin", nullable = true)
    private Boolean shouldChangePasswordOnNextLogin;
    
    @Basic
	@Column(name = "PasswordTokenExpiration", nullable = true)
    private Date passwordTokenExpiration;
    
    @Basic
    @Column(name = "PhoneNumber", nullable = true, length = 32)
    private String phoneNumber;
    
    @Basic
    @Column(name = "ProfilePictureId", nullable = true)
    private Long profilePictureId;
    
    @Basic
    @Column(name = "LastLoginTime", nullable = true)
    private Date lastLoginTime;
    
    @Basic
    @Column(name = "AccessFailedCount", nullable = true)
    private Integer accessFailedCount;
    
    @Basic
    @Column(name = "AuthenticationSource", nullable = true, length = 64)
    private String authenticationSource;
    
    @Basic
    @Column(name = "IsEmailConfirmed", nullable = true)
    private Boolean isEmailConfirmed;
    
    @Basic
    @Column(name = "EmailConfirmationCode", nullable = true, length = 328)
    private String emailConfirmationCode;
    
    @Basic
    @Column(name = "IsLockoutEnabled", nullable = true)
    private Boolean isLockoutEnabled;
    
    @Basic
    @Column(name = "LockoutEndDateUtc", nullable = true)
    private Date lockoutEndDateUtc;
    
    @Basic
    @Column(name = "IsPhoneNumberConfirmed", nullable = true)
    private String isPhoneNumberConfirmed;
    
    @Basic
    @Column(name = "TwoFactorEnabled", nullable = true)
    private Boolean isTwoFactorEnabled;
    
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<DashboardEntity> dashboardSet = new HashSet<DashboardEntity>(); 
  	
  	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL) 
	private Set<DashboardversionEntity> dashboardversionSet = new HashSet<DashboardversionEntity>(); 
  
  	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<ReportEntity> reportSet = new HashSet<ReportEntity>(); 
  	
  	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<ReportversionEntity> reportversionSet = new HashSet<ReportversionEntity>(); 
  	
  	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<UserpermissionEntity> userpermissionSet = new HashSet<UserpermissionEntity>();

  	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<UserroleEntity> userroleSet = new HashSet<UserroleEntity>(); 
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<ReportuserEntity> reportuserSet = new HashSet<ReportuserEntity>();
  	
  	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) 
  	private Set<DashboarduserEntity> dashboarduserSet = new HashSet<DashboarduserEntity>(); 
  	
  	public void addUserpermission(UserpermissionEntity userpermission) {
		userpermissionSet.add(userpermission);
		userpermission.setUser(this);
	}

	public void removeUserpermission(UserpermissionEntity userpermission) {
		userpermissionSet.remove(userpermission);
		userpermission.setUser(null);
	}

	public void addUserrole(UserroleEntity userrole) {
		userroleSet.add(userrole);
		userrole.setUser(this);
	}

	public void removeUserrole(UserroleEntity userrole) {
		userroleSet.remove(userrole);
		userrole.setUser(null);
	}
	
	public void addReportversion(ReportversionEntity reportVersion) {
		reportversionSet.add(reportVersion);
		reportVersion.setUser(this);
	}

	public void removeReportversion(ReportversionEntity reportVersion) {
		reportversionSet.remove(reportVersion);
		reportVersion.setUser(null);
	}
	
	public void addReport(ReportEntity report) {
		reportSet.add(report);
		report.setUser(this);
	}

	public void removeReport(ReportEntity report) {
		reportSet.remove(report);
		report.setUser(null);
	}
	
	public void addReportuser(ReportuserEntity reportUser) {
		reportuserSet.add(reportUser);
		reportUser.setUser(this);
	}

	public void removeReportuser(ReportuserEntity reportUser) {
		reportuserSet.remove(reportUser);
		reportUser.setUser(null);
	}
	
	public void addDashboardversion(DashboardversionEntity dashboardVersion) {
		dashboardversionSet.add(dashboardVersion);
		dashboardVersion.setUser(this);
	}

	public void removeDashboardversion(DashboardversionEntity dashboardVersion) {
		dashboardversionSet.remove(dashboardVersion);
		dashboardVersion.setUser(null);
	}
	
	public void addDashboard(DashboardEntity dashboard) {
		dashboardSet.add(dashboard);
		dashboard.setUser(this);
	}

	public void removeDashboard(DashboardEntity dashboard) {
		dashboardSet.remove(dashboard);
		dashboard.setUser(null);
	}
	
	public void addDashboarduser(DashboarduserEntity dashboarduser) {
		dashboarduserSet.add(dashboarduser);
		dashboarduser.setUser(this);
	}

	public void removeDashboarduser(DashboarduserEntity dashboarduser) {
		dashboarduserSet.remove(dashboarduser);
		dashboarduser.setUser(null);
	}
	
	
}

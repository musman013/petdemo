package com.fastcode.demopet;

import com.fastcode.demopet.domain.model.*;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;
import com.fastcode.demopet.domain.authorization.permission.IPermissionManager;
import com.fastcode.demopet.domain.authorization.rolepermission.IRolepermissionManager;
import com.fastcode.demopet.domain.authorization.userrole.IUserroleManager;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.authorization.userpreference.IUserpreferenceManager;
import com.fastcode.demopet.domain.authorization.role.IRoleManager;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.processmanagement.ActIdUserMapper;
import com.fastcode.demopet.application.processmanagement.FlowableIdentityService;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("bootstrap")
public class AppStartupRunner implements ApplicationRunner {

	@Autowired
	private FlowableIdentityService idmIdentityService;

	@Autowired
	private ActIdUserMapper actIdUserMapper;

	@Autowired 
	private IUserpreferenceManager _userpreferenceManager;

	@Autowired
	private Environment environment;

	@Autowired
	private IPermissionManager permissionManager;

	@Autowired
	private IRoleManager roleManager;

	@Autowired
	private IUserManager userManager;

	@Autowired
	private IUserroleManager userroleManager;

	@Autowired
	private IRolepermissionManager rolepermissionManager;

	@Autowired
	private LoggingHelper loggingHelper;

	@Autowired
	private PasswordEncoder pEncoder;

	@Override
	public void run(ApplicationArguments args) {

		idmIdentityService.deleteAllUsersGroupsPrivileges();

		System.out.println("*****************Creating Default Users/Roles/Permissions *************************");

		// Create permissions for default entities
		loggingHelper.getLogger().info("Creating the data in the database");

		// Create roles
		RoleEntity role = new RoleEntity();
		role.setName("ROLE_Admin");
		role.setDisplayName("Role1");
		role = roleManager.create(role);

		RoleEntity role1 = new RoleEntity();
		role1.setName("ROLE_Owner");
		role1.setDisplayName("Role2");
		role1 = roleManager.create(role1);

		RoleEntity role2 = new RoleEntity();
		role2.setName("ROLE_Vet");
		role2.setDisplayName("Role3");
		role2 = roleManager.create(role2);
		addDefaultUser(role);
		idmIdentityService.createGroup("ROLE_Admin");
		idmIdentityService.createGroup("ROLE_Owner");
		idmIdentityService.createGroup("ROLE_Vet");

		List<String> entityList = new ArrayList<String>();
		entityList.add("role");
		entityList.add("permission");
		entityList.add("rolepermission");

		entityList.add("user");
		entityList.add("userpermission");
		entityList.add("userrole");
		entityList.add("email");
		entityList.add("emailVariable");
		entityList.add("jobDetails");
		entityList.add("triggerDetails");

		entityList.add("pets");
		entityList.add("types");
		entityList.add("specialties");
		entityList.add("vets");
		entityList.add("visits");
		entityList.add("invoices");
		entityList.add("vetspecialties");
		entityList.add("owners");

		entityList.add("report");
		entityList.add("dashboard");
		entityList.add("reportdashboard");

		for(String entity: entityList) {
			if(!environment.getProperty("fastCode.auth.method").equals("database") && entity.equals("user"))
				addEntityPermissions(entity, role.getId(), true);
			else
				addEntityPermissions(entity, role.getId(), false);
		}


		assignEntityPermissions("visits", role1);
		assignEntityReadPermission("invoices", role1);
		//		assignEntityReadPermission("visits", role1);
		//		assignEntityReadPermission("invoices", role2);
		assignEntityReadPermission("visits", role2);

		addEntityHistoryPermissions("entityHistory", role.getId());
		addAuditTrailPermission("auditTrail", role.getId());
		addFlowablePrivileges(role.getId());
		assignFlowableTaskPermission(role2);
		assignFlowableTaskPermission(role1);
		loggingHelper.getLogger().info("Completed creating the data in the database");

	}

	private void assignFlowableTaskPermission(RoleEntity role)
	{
		PermissionEntity pe = permissionManager.findByPermissionName("access-task");

		RolepermissionEntity pe2RP= new RolepermissionEntity(pe.getId(), role.getId());
		rolepermissionManager.create(pe2RP);

		idmIdentityService.addGroupPrivilegeMapping(role.getName(), pe.getName());
	}


	private void assignEntityPermissions(String entity, RoleEntity role)
	{

		PermissionEntity pe= permissionManager.findByPermissionName(entity.toUpperCase() + "ENTITY_UPDATE");
		RolepermissionEntity pe2RP= new RolepermissionEntity(pe.getId(), role.getId());
		rolepermissionManager.create(pe2RP);

		PermissionEntity pe1 = permissionManager.findByPermissionName(entity.toUpperCase() + "ENTITY_READ");
		RolepermissionEntity pe2RP1 = new RolepermissionEntity(pe1.getId(), role.getId());
		rolepermissionManager.create(pe2RP1);

		PermissionEntity pe2= permissionManager.findByPermissionName(entity.toUpperCase() + "ENTITY_CREATE");
		RolepermissionEntity pe2RP2 = new RolepermissionEntity(pe2.getId(), role.getId());
		rolepermissionManager.create(pe2RP2);

		PermissionEntity pe3= permissionManager.findByPermissionName(entity.toUpperCase() + "ENTITY_DELETE");
		RolepermissionEntity pe2RP3= new RolepermissionEntity(pe3.getId(), role.getId());
		rolepermissionManager.create(pe2RP3);

	}
	private void assignEntityReadPermission(String entity, RoleEntity role)
	{
		PermissionEntity pe= permissionManager.findByPermissionName(entity.toUpperCase() + "ENTITY_READ");

		RolepermissionEntity pe2RP = new RolepermissionEntity(pe.getId(), role.getId());
		rolepermissionManager.create(pe2RP);
	}

	private void addEntityHistoryPermissions(String entity, long roleId) {
		PermissionEntity pe = new PermissionEntity(entity.toUpperCase() , entity);
		pe = permissionManager.create(pe);
		RolepermissionEntity pe2RP = new RolepermissionEntity(pe.getId(), roleId);
		rolepermissionManager.create(pe2RP);
	}

	private void addAuditTrailPermission(String entity, long roleId) {
		PermissionEntity pe = new PermissionEntity(entity.toUpperCase() , entity);
		pe = permissionManager.create(pe);
		RolepermissionEntity pe2RP = new RolepermissionEntity(pe.getId(), roleId);
		rolepermissionManager.create(pe2RP);
	}

	private void addEntityPermissions(String entity, long roleId,boolean readOnly) {
		if(readOnly)
		{
			PermissionEntity pe2 = new PermissionEntity(entity.toUpperCase() + "ENTITY_READ", "read " + entity);
			pe2 = permissionManager.create(pe2);
			RolepermissionEntity pe2RP = new RolepermissionEntity(pe2.getId(), roleId);
			rolepermissionManager.create(pe2RP);
		}
		else
		{
			PermissionEntity pe1 = new PermissionEntity(entity.toUpperCase() + "ENTITY_CREATE", "create " + entity);
			PermissionEntity pe2 = new PermissionEntity(entity.toUpperCase() + "ENTITY_READ", "read " + entity);
			PermissionEntity pe3 = new PermissionEntity(entity.toUpperCase() + "ENTITY_DELETE", "delete " + entity);
			PermissionEntity pe4 = new PermissionEntity(entity.toUpperCase() + "ENTITY_UPDATE", "update " + entity);


			pe1 = permissionManager.create(pe1);
			pe2 = permissionManager.create(pe2);
			pe3 = permissionManager.create(pe3);
			pe4 = permissionManager.create(pe4);

			RolepermissionEntity pe1RP = new RolepermissionEntity(pe1.getId(), roleId);
			RolepermissionEntity pe2RP = new RolepermissionEntity(pe2.getId(), roleId);
			RolepermissionEntity pe3RP = new RolepermissionEntity(pe3.getId(), roleId);
			RolepermissionEntity pe4RP = new RolepermissionEntity(pe4.getId(), roleId);

			rolepermissionManager.create(pe1RP);
			rolepermissionManager.create(pe2RP);
			rolepermissionManager.create(pe3RP);
			rolepermissionManager.create(pe4RP);
		}
	}

	private void addFlowablePrivileges(long roleId) {
		PermissionEntity pe5 = new PermissionEntity("access-idm","");
		PermissionEntity pe6 = new PermissionEntity("access-admin","");
		PermissionEntity pe7 = new PermissionEntity("access-modeler","");
		PermissionEntity pe8 = new PermissionEntity("access-task","");
		PermissionEntity pe9 = new PermissionEntity("access-rest-api","");

		pe5 = permissionManager.create(pe5);
		pe6 = permissionManager.create(pe6);
		pe7 = permissionManager.create(pe7);
		pe8 = permissionManager.create(pe8);
		pe9 = permissionManager.create(pe9);

		RolepermissionEntity pe5RP = new RolepermissionEntity(pe5.getId(), roleId);
		RolepermissionEntity pe6RP = new RolepermissionEntity(pe6.getId(), roleId);
		RolepermissionEntity pe7RP = new RolepermissionEntity(pe7.getId(), roleId);
		RolepermissionEntity pe8RP = new RolepermissionEntity(pe8.getId(), roleId);
		RolepermissionEntity pe9RP = new RolepermissionEntity(pe9.getId(), roleId);

		rolepermissionManager.create(pe5RP);
		rolepermissionManager.create(pe6RP);
		rolepermissionManager.create(pe7RP);
		rolepermissionManager.create(pe8RP);
		rolepermissionManager.create(pe9RP);

		idmIdentityService.createPrivilege("access-idm");
		idmIdentityService.createPrivilege("access-admin");
		idmIdentityService.createPrivilege("access-modeler");
		idmIdentityService.createPrivilege("access-task");
		idmIdentityService.createPrivilege("access-rest-api");

		idmIdentityService.addGroupPrivilegeMapping("ROLE_Admin", pe5.getName());
		idmIdentityService.addGroupPrivilegeMapping("ROLE_Admin", pe6.getName());
		idmIdentityService.addGroupPrivilegeMapping("ROLE_Admin", pe7.getName());
		idmIdentityService.addGroupPrivilegeMapping("ROLE_Admin", pe8.getName());
		idmIdentityService.addGroupPrivilegeMapping("ROLE_Admin", pe9.getName());

	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void addDefaultUser(RoleEntity role) {
		UserEntity admin = new UserEntity();
		admin.setFirstName("test");
		admin.setLastName("admin");
		admin.setUserName("admin");
		admin.setEmailAddress("admin@demo.com");
		admin.setPassword(pEncoder.encode("secret"));
		admin.setIsActive(true);
		admin = userManager.create(admin);
		
		createUserpreference(admin);

		UserroleEntity urole = new UserroleEntity();
		urole.setRoleId(role.getId());
		urole.setUserId(admin.getId());
		urole=userroleManager.create(urole);

		ActIdUserEntity actIdUser = actIdUserMapper.createUsersEntityToActIdUserEntity(admin);
 		actIdUser.setPwd("test");
 		idmIdentityService.createUser(admin, actIdUser);
 		idmIdentityService.addUserGroupMapping("admin", role.getName());

	}

	private void createUserpreference(UserEntity user)
	{
		UserpreferenceEntity userpreference = new UserpreferenceEntity();
		userpreference.setTheme("default-theme");
		userpreference.setLanguage("en");
		userpreference.setId(user.getId());
		userpreference.setUser(user);

		_userpreferenceManager.create(userpreference);
	}

}

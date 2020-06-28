package com.fastcode.demopet.application.processmanagement;

import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;
import com.fastcode.demopet.domain.processmanagement.users.IActIdUserManager;
import com.fastcode.demopet.security.SecurityUtils;
import com.fastcode.demopet.domain.processmanagement.groups.ActIdGroupEntity;
import com.fastcode.demopet.domain.processmanagement.groups.IActIdGroupManager;
import com.fastcode.demopet.domain.processmanagement.memberships.ActIdMembershipEntity;
import com.fastcode.demopet.domain.processmanagement.memberships.IActIdMembershipManager;
import com.fastcode.demopet.domain.processmanagement.privilegemappings.ActIdPrivMappingEntity;
import com.fastcode.demopet.domain.processmanagement.privilegemappings.IActIdPrivMappingManager;
import com.fastcode.demopet.domain.processmanagement.privileges.ActIdPrivEntity;
import com.fastcode.demopet.domain.processmanagement.privileges.IActIdPrivManager;
import com.fastcode.demopet.domain.processmanagement.tokens.ActIdTokenEntity;
import com.fastcode.demopet.domain.processmanagement.tokens.IActIdTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.util.InetAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;

import javax.persistence.EntityExistsException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

@Service
public class FlowableIdentityService {
    private static final int DEFAULT_SERIES_LENGTH = 16;

    private static final int DEFAULT_TOKEN_LENGTH = 16;
    private static final String COOKIE_NAME = "FLOWABLE_REMEMBER_ME";

    private SecureRandom random;
    private String tokenDomain;

    protected static final String DELIMITER = ":";

    private String[] flowablePrivileges = new String[]{"access-idm", "access-admin", "access-modeler", "access-task", "access-rest-api"};

    public FlowableIdentityService() {
        random = new SecureRandom();
    }

    @Autowired
    protected IActIdTokenManager _tokenManager;
    public IActIdTokenManager get_tokenManager() {
        return _tokenManager;
    }

    public void set_tokenManager(IActIdTokenManager _tokenManager) {
        this._tokenManager = _tokenManager;
    }

    @Autowired
    protected IActIdUserManager _actIdUserManager;

    public IActIdUserManager get_actIdUserManager() {
    return _actIdUserManager;
    }

    public void set_actIdUserManager(IActIdUserManager _actIdUserManager) {
    this._actIdUserManager = _actIdUserManager;
    }
    @Autowired
    protected IActIdGroupManager _actIdGroupManager;

    public IActIdGroupManager get_actIdGroupManager() {
        return _actIdGroupManager;
    }

    public void set_actIdGroupManager(IActIdGroupManager _actIdGroupManager) {
        this._actIdGroupManager = _actIdGroupManager;
    }

    @Autowired
    protected IActIdMembershipManager _actIdMembershipManager;

    public IActIdMembershipManager get_actIdMembershipManager() {
        return _actIdMembershipManager;
    }

    public void set_actIdMembershipManager(IActIdMembershipManager _actIdMembershipManager) {
        this._actIdMembershipManager = _actIdMembershipManager;
    }

    @Autowired
    protected IActIdPrivManager _actIdPrivManager;

    public IActIdPrivManager get_actIdPrivManager() {
        return _actIdPrivManager;
    }

    public void set_actIdPrivManager(IActIdPrivManager _actIdPrivManager) {
        this._actIdPrivManager = _actIdPrivManager;
    }
    //_actIdPrivMappingManager
    @Autowired
    protected IActIdPrivMappingManager _actIdPrivMappingManager;

    public IActIdPrivMappingManager get_actIdPrivMappingManager() {
        return _actIdPrivMappingManager;
    }

    public void set_actIdPrivMappingManager(IActIdPrivMappingManager _actIdPrivMappingManager) {
        this._actIdPrivMappingManager = _actIdPrivMappingManager;
    }
    
    public UserEntityImpl getFlowableUser() {
    	ActIdUserEntity existingUser = _actIdUserManager.findByUserId(SecurityUtils.getCurrentUserLogin().orElse(null));
//		UserEntity existingUser = userAppService.getUser();
		UserEntityImpl user = new UserEntityImpl();
		user.setFirstName(existingUser.getFirst());
		user.setLastName(existingUser.getLast());
		user.setId(existingUser.getId());
		user.setPassword(existingUser.getPwd());
		user.setEmail(existingUser.getEmail());
		
		return user;
    }
    
    
    public void createUser(UserEntity createdUser, ActIdUserEntity actIdUser) {
    //    if (StringUtils.isBlank(actIdUser.getId()) || StringUtils.isBlank(actIdUser.getFirst())) {
    //        //User not valid
    //        throw new IllegalArgumentException("Not a valid flowable type user.");
    //    }
    //    if (StringUtils.isNotBlank(actIdUser.getEmail()) && _actIdUserManager.findByUserEmail(actIdUser.getEmail()) != null) {
    //        //User already registered
    //        throw new EntityExistsException("Flowable user already exists.");
     //   }
        if (StringUtils.isNotBlank(actIdUser.getId()) && _actIdUserManager.findByUserId(actIdUser.getId()) != null) {
            //User already registered
            throw new EntityExistsException("Flowable user already exists.");
        }
        _actIdUserManager.create(actIdUser);
     //   //Check if user is associated with a role
     //   RoleEntity role = createdUser.getRole();
     //   if(role != null)
     //   {
     //       //Check if the role already exists in the flowable group table
     //       ActIdGroupEntity actIdGroup = _actIdGroupManager.findByGroupId(role.getName());
     //       //Group doesn't exist. Create group
     //       if(actIdGroup == null) {
      //          createGroup(role.getName());
     //       }
      //      createMembership(role.getName(), createdUser.getUserName());
      //  }
    }

	public void updateUser(UserEntity updatedUser, ActIdUserEntity actIdUser) {
		//Check if user already exists
		ActIdUserEntity existingUser = _actIdUserManager.findByUserId(actIdUser.getId());
		if (StringUtils.isNotBlank(actIdUser.getId()) && existingUser != null) {
			//Increment the revision by 1
			actIdUser.setRev(existingUser.getRev() + 1);
			_actIdUserManager.update(actIdUser);
		}
	}

	public void deleteUser(String id) {

		List<ActIdMembershipEntity> actIdMemberships = _actIdMembershipManager.findAllByUserId(id);

		for (ActIdMembershipEntity actIdMembership: actIdMemberships) {
			_actIdMembershipManager.delete(actIdMembership);
		}

		List<ActIdPrivMappingEntity> actIdPrivMappings = _actIdPrivMappingManager.findAllByUserId(id);

		for (ActIdPrivMappingEntity actIdPrivMapping: actIdPrivMappings) {
			_actIdPrivMappingManager.delete(actIdPrivMapping);
		}
		ActIdUserEntity actIdUser = _actIdUserManager.findByUserId(id);
		if(actIdUser != null) {
			_actIdUserManager.delete(actIdUser);
		}
	}

	public void deleteAllUsersGroupsPrivileges() {
		_actIdMembershipManager.deleteAll();
		_actIdPrivMappingManager.deleteAll();
		_actIdPrivManager.deleteAll();
		_actIdGroupManager.deleteAll();
		_actIdUserManager.deleteAll();
	}
	public void createGroup(String groupId) {
		if (groupId == null) {
			throw new IllegalArgumentException("groupid is null");
		}
		ActIdGroupEntity actIdGroup = new ActIdGroupEntity();
		actIdGroup.setId(groupId);
		actIdGroup.setName(groupId);
		actIdGroup.setRev(0L);
		actIdGroup.setType(null);
		_actIdGroupManager.create(actIdGroup);
	}

	public void updateGroup(String groupId, String oldRoleName) {
		//Check if group already exists
		ActIdGroupEntity existingGroup = _actIdGroupManager.findByGroupId(oldRoleName);
		if (existingGroup != null && groupId != null) {
			if (oldRoleName != null && !oldRoleName.equalsIgnoreCase(groupId)) {
				//Update relationship with old role/group if applicable
				List<ActIdMembershipEntity> actIdMemberships = _actIdMembershipManager.findAllByGroupId(oldRoleName);
				for (ActIdMembershipEntity actIdMembership : actIdMemberships) {
					createMembership(groupId, actIdMembership.getUserId());
					_actIdMembershipManager.delete(actIdMembership);
				}
				//Update Privilege mapping
				List<ActIdPrivMappingEntity> actIdPrivMappings = _actIdPrivMappingManager.findAllByGroupId(oldRoleName);
				for (ActIdPrivMappingEntity actIdPrivMapping : actIdPrivMappings) {
					actIdPrivMapping.setGroupId(groupId);
					updatePrivilegeMapping(actIdPrivMapping);
				}
			}

			//Increment the revision by 1
			ActIdGroupEntity actIdGroup = new ActIdGroupEntity();
			actIdGroup.setId(groupId);
			actIdGroup.setName(groupId);
			actIdGroup.setRev(existingGroup.getRev() + 1);
			actIdGroup.setType(null);
			_actIdGroupManager.create(actIdGroup);

			_actIdGroupManager.delete(existingGroup);
		}
	}

	public void deleteGroup(String groupId) {
		//Delete membership
		List<ActIdMembershipEntity> actIdMemberships = _actIdMembershipManager.findAllByGroupId(groupId);

		for (ActIdMembershipEntity actIdMembership: actIdMemberships) {
			_actIdMembershipManager.delete(actIdMembership);
		}
		//Delete group privilege mapping
		List<ActIdPrivMappingEntity> actIdPrivMappings = _actIdPrivMappingManager.findAllByGroupId(groupId);
		for (ActIdPrivMappingEntity actIdPrivMapping : actIdPrivMappings) {
			_actIdPrivMappingManager.delete(actIdPrivMapping);
		}

		ActIdGroupEntity actIdGroup = _actIdGroupManager.findByGroupId(groupId);
		if(actIdGroup != null) {
			_actIdGroupManager.delete(actIdGroup);
		}
	}

	public void createMembership(String groupId, String userId) {
		if (groupId == null) {
			//User not valid
			throw new IllegalArgumentException("groupid is null");
		}

		if (userId == null) {
			//User not valid
			throw new IllegalArgumentException("userId is null");
		}
		//MembershipId membershipId = new MembershipId(groupId, userId);
		ActIdMembershipEntity actIdMembership = new ActIdMembershipEntity();
		actIdMembership.setGroupId(groupId);
		actIdMembership.setUserId(userId);
		_actIdMembershipManager.create(actIdMembership);
	}

	public void deleteMembership(String userId) {
		List<ActIdMembershipEntity> actIdMemberships = _actIdMembershipManager.findAllByUserId(userId);

		for (ActIdMembershipEntity actIdMembership: actIdMemberships) {
			_actIdMembershipManager.delete(actIdMembership);
		}
	}

	public void createPrivilege(String name) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(name::equalsIgnoreCase);
		if (result) {
			ActIdPrivEntity actIdPrivilege = new ActIdPrivEntity();
			actIdPrivilege.setId(RandomStringUUID());
			actIdPrivilege.setName(name);
			_actIdPrivManager.create(actIdPrivilege);
		}
	}

	public void addUserGroupMapping(String userId, String groupName) {

		ActIdMembershipEntity actIdGroupMapping = newGroupMapping(groupName);
		actIdGroupMapping.setUserId(userId);
		_actIdMembershipManager.create(actIdGroupMapping);

	}

	public void updateUserGroupMapping(String userId, String groupName) {
		ActIdMembershipEntity actIdPrivMapping = _actIdMembershipManager.findByUserGroupId(userId, groupName);
		_actIdMembershipManager.update(actIdPrivMapping);
	}

	public void deleteUserGroupMapping(String userId, String groupName) {
		ActIdMembershipEntity actIdMembership= _actIdMembershipManager.findByUserGroupId(userId, groupName);
		_actIdMembershipManager.delete(actIdMembership);
	}

	private ActIdMembershipEntity newGroupMapping(String groupName) {
		ActIdGroupEntity actIdGroup = _actIdGroupManager.findByGroupId(groupName);
		if(actIdGroup == null) {
			createGroup(groupName);
			actIdGroup = _actIdGroupManager.findByGroupId(groupName);
		}

		ActIdMembershipEntity actIdGroupMapping = new ActIdMembershipEntity();
		actIdGroupMapping.setGroupId(groupName);
		return actIdGroupMapping;
	}

	public void addUserPrivilegeMapping(String userId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = newPrivMapping(privName);
			actIdPrivMapping.setUserId(userId);
			_actIdPrivMappingManager.create(actIdPrivMapping);
		}
	}

	public void updateUserPrivilegeMapping(String userId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = _actIdPrivMappingManager.findByUserPrivilege(userId, privName);
			_actIdPrivMappingManager.update(actIdPrivMapping);
		}
	}

	public void deleteUserPrivilegeMapping(String userId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = _actIdPrivMappingManager.findByUserPrivilege(userId, privName);
			_actIdPrivMappingManager.delete(actIdPrivMapping);
		}
	}

	public void updatePrivilegeMapping(ActIdPrivMappingEntity actIdPrivMapping) {
		ActIdPrivEntity actIdPrivilege = actIdPrivMapping.getActIdPriv();
		if (actIdPrivilege != null) {
			boolean result = Arrays.stream(flowablePrivileges).anyMatch(actIdPrivilege.getName()::equalsIgnoreCase);
			if (result) {
				_actIdPrivMappingManager.update(actIdPrivMapping);
			}
		}
	}

	public void addGroupPrivilegeMapping(String groupId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = newPrivMapping(privName);
			actIdPrivMapping.setGroupId(groupId);
			_actIdPrivMappingManager.create(actIdPrivMapping);
		}
	}

	public void updateGroupPrivilegeMapping(String groupId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = _actIdPrivMappingManager.findByGroupPrivilege(groupId, privName);
			_actIdPrivMappingManager.update(actIdPrivMapping);
		}
	}

	private ActIdPrivMappingEntity newPrivMapping(String privName) {
		ActIdPrivEntity actIdPrivilege = _actIdPrivManager.findByName(privName);
		if(actIdPrivilege == null) {
			createPrivilege(privName);
			actIdPrivilege = _actIdPrivManager.findByName(privName);
		}

		ActIdPrivMappingEntity actIdPrivMapping = new ActIdPrivMappingEntity();
		actIdPrivMapping.setId(RandomStringUUID());
		actIdPrivMapping.setActIdPriv(actIdPrivilege);
		return actIdPrivMapping;
	}

	public void deleteGroupPrivilegeMapping(String groupId, String privName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(privName::equalsIgnoreCase);
		if (result) {
			ActIdPrivMappingEntity actIdPrivMapping = _actIdPrivMappingManager.findByGroupPrivilege(groupId, privName);
			_actIdPrivMappingManager.delete(actIdPrivMapping);
		}
	}

	public void deletePrivilege(String name) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(name::equalsIgnoreCase);
		if (result) {
			ActIdPrivEntity actIdPrivilege = _actIdPrivManager.findByName(name);
			_actIdPrivManager.delete(actIdPrivilege);
		}
	}

	public void updatePrivilege(String oldName, String newName) {
		boolean result = Arrays.stream(flowablePrivileges).anyMatch(newName::equalsIgnoreCase);
		if (result) {
			ActIdPrivEntity actIdPrivilege = _actIdPrivManager.findByName(oldName);
			actIdPrivilege.setName(newName);
			_actIdPrivManager.update(actIdPrivilege);
		}
	}
    public String createTokenAndCookie (String userId, HttpServletRequest request, HttpServletResponse response) {
        ActIdTokenEntity token = createToken(userId, request.getRemoteAddr(), request.getHeader("User-Agent"));
        return setCookie(new String[] { token.getId(), token.getTokenValue() }, request, response);
    }
    private ActIdTokenEntity createToken(String userId, String remoteAddress, String userAgent) {
        String tokenId = generateSeriesData();
        if (tokenId == null) {
            throw new IllegalArgumentException("tokenId is null");
        }
        ActIdTokenEntity token = _tokenManager.generateNewToken(tokenId);
        token.setTokenValue(generateTokenData());
        token.setTokenDate(new Date());
        token.setIpAddress(remoteAddress);
        token.setUserAgent(userAgent);
        token.setUserId(userId);

        try {
            saveToken(token);
            return token;
        } catch (DataAccessException e) {
            //LOGGER.error("Failed to save persistent token ", e);
            return token;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveToken(ActIdTokenEntity token) {
        if (token == null) {
            throw new IllegalArgumentException("token is null");
        }
        if(_tokenManager.isNewToken(token)) {
            _tokenManager.insertToken(token);
        }
        else {
            _tokenManager.updateToken(token);
        }
    }

    private String setCookie(String[] tokens, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
        //31 days
        int maxAge = 2678400;
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        if (tokenDomain == null) {
            String domain = request.getRemoteAddr();
            boolean isIp = InetAddressUtils.isIPv4Address(domain);
            if (!isIp){
                String[] dis = domain.split(".");
                if (dis.length>=2){
                    tokenDomain = "."+ dis[dis.length-2]+"."+ dis[dis.length-1];
                }
            }
        }
        if(tokenDomain != null) {
            cookie.setDomain(tokenDomain);
        }
        else
        {
            String localhostDomain = "localhost";
            cookie.setDomain(localhostDomain);
        }

        return cookieValue;
    }

    private String encodeCookie(String[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            try {
                tokens[i] = URLEncoder.encode(tokens[i], StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                //logger.error(e.getMessage(), e);
            }
        }
        String cookieValueAsPlainText = String.join(DELIMITER, tokens);
        return Base64.getEncoder().encodeToString(cookieValueAsPlainText.getBytes());
    }

    private String generateSeriesData() {
        return generateRandomWithoutSlash(DEFAULT_SERIES_LENGTH);
    }

    private String generateTokenData() {
        return generateRandomWithoutSlash(DEFAULT_TOKEN_LENGTH);
    }

    private String generateRandomWithoutSlash(int size) {
        String data = generateRandom(size);
        while (data.contains("/")) {
            data = generateRandom(size);
        }
        return data;
    }

    private String generateRandom(int size) {
        byte[] s = new byte[size];
        random.nextBytes(s);
        return new String(Base64.getEncoder().encode(s));
    }

    private String RandomStringUUID() {
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}

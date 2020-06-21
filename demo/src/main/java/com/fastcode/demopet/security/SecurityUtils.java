package com.fastcode.demopet.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.fastcode.demopet.domain.model.RolepermissionEntity;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.UserpermissionEntity;
import com.fastcode.demopet.domain.model.UserroleEntity;
@Component
public class SecurityUtils {

    public List<String> getAllPermissionsFromUserAndRole(UserEntity user) {

		List<String> permissions = new ArrayList<>();
        Set<UserroleEntity> ure = user.getUserroleSet();
        Iterator rIterator = ure.iterator();
		while (rIterator.hasNext()) {
            UserroleEntity re = (UserroleEntity) rIterator.next();
            Set<RolepermissionEntity> srp= re.getRole().getRolepermissionSet();
            for (RolepermissionEntity item : srp) {
				permissions.add(item.getPermission().getName());
            }
		}
		
		Set<UserpermissionEntity> spe = user.getUserpermissionSet();
        Iterator pIterator = spe.iterator();
		while (pIterator.hasNext()) {
            UserpermissionEntity pe = (UserpermissionEntity) pIterator.next();
            
            if(permissions.contains(pe.getPermission().getName()) && (pe.getRevoked() != null && pe.getRevoked()))
            {
            	permissions.remove(pe.getPermission().getName());
            }
            if(!permissions.contains(pe.getPermission().getName()) && (pe.getRevoked()==null || !pe.getRevoked()))
            {
            	permissions.add(pe.getPermission().getName());
			
            }
         
		}
		
		return permissions
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}
	
	public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }
                return null;
            });
    }

}

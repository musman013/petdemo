package com.fastcode.demopet.domain.authorization.userrole;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.fastcode.demopet.domain.model.UserroleEntity;
import com.fastcode.demopet.domain.model.UserroleId;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.irepository.IRoleRepository;
import com.fastcode.demopet.domain.model.RoleEntity;
import com.fastcode.demopet.domain.irepository.IUserroleRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class UserroleManager implements IUserroleManager {

    @Autowired
    IUserroleRepository  _userroleRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    @Autowired
	IRoleRepository  _roleRepository;
    
	public UserroleEntity create(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}

	public void delete(UserroleEntity userrole) {

		_userroleRepository.delete(userrole);	
	}

	public UserroleEntity update(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}
	
	public List<UserroleEntity> findByUserId(Long userId)
	{
		return _userroleRepository.findByUserId(userId);
	}

	public UserroleEntity findById(UserroleId userroleId) {
    	Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole;
		} else {
		    return null;
		}

	}

	public Page<UserroleEntity> findAll(Predicate predicate, Pageable pageable) {

		return _userroleRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity getUser(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getUser();
		} else {
		    return null;
		}
	}
  
   //Role
	public RoleEntity getRole(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getRole();
		} else {
		    return null;
		}
	}
}

package com.fastcode.demopet.domain.authorization.user;

import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.irepository.IUserRepository;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public class UserManager implements IUserManager {

	@Autowired
	IUserRepository  _userRepository;

	public UserEntity create(UserEntity user) {

		return _userRepository.save(user);
	}

	public void delete(UserEntity user) {

		_userRepository.delete(user);	
	}

	public UserEntity update(UserEntity user) {

		return _userRepository.save(user);
	}

	public UserEntity findById(Long  userId) {
		Optional<UserEntity> dbUser= _userRepository.findById(userId);
		if(dbUser.isPresent()) {
			UserEntity existingUser = dbUser.get();
			return existingUser;
		} else {
			return null;
		}
	}

	public Page<UserEntity> findAll(Predicate predicate, Pageable pageable) {

		return _userRepository.findAll(predicate,pageable);
	}

	public UserEntity findByUserName(String userName) {
		return  _userRepository.findByUserName(userName);
	}

	public UserEntity findByEmailAddress(String emailAddress) {
		return  _userRepository.findByEmailAddress(emailAddress);
	}

}


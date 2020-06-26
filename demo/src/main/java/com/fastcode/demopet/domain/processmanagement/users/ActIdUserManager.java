package com.fastcode.demopet.domain.processmanagement.users;

import com.fastcode.demopet.domain.irepository.IActIdGroupRepository;
import com.fastcode.demopet.domain.irepository.IActIdUserRepository;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ActIdUserManager implements IActIdUserManager {

	@Autowired
	private IActIdUserRepository _actIdUsersRepository;
	
	@Autowired
	private IActIdGroupRepository _groupRepository;
	
	@Autowired
	private LoggingHelper logHelper;

	@Override
	public void create(ActIdUserEntity actIdUser) {
		_actIdUsersRepository.save(actIdUser);
	}

	@Override
	public void delete(ActIdUserEntity actIdUser) {
		_actIdUsersRepository.delete(actIdUser);
	}

	@Override
	public void update(ActIdUserEntity actIdUser) {
		_actIdUsersRepository.save(actIdUser);
	}

	@Override
	public void deleteAll() {
		_actIdUsersRepository.deleteAll();
	}

	@Override
	public ActIdUserEntity findByUserId(String userId) {
		return  _actIdUsersRepository.findByUserId(userId);
	}

	@Override
	public ActIdUserEntity findByUserEmail(String email) {
		return  _actIdUsersRepository.findByUserEmail(email);
	}


}

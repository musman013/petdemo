package com.fastcode.demopet.domain.processmanagement.privileges;

import com.fastcode.demopet.domain.irepository.IActIdPrivRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActIdPrivManager implements IActIdPrivManager {

	@Autowired
	private IActIdPrivRepository _privRepository;

	@Override
	public void create(ActIdPrivEntity actIdPrivilege) {
		_privRepository.save(actIdPrivilege);
	}

	@Override
	public ActIdPrivEntity findByName(String name) {
		return _privRepository.findByName(name);
	}

	@Override
	public void delete(ActIdPrivEntity actIdPrivilege) {
		_privRepository.delete(actIdPrivilege);
	}

	@Override
	public void update(ActIdPrivEntity actIdPrivilege) {
		_privRepository.save(actIdPrivilege);
	}

	@Override
	public void deleteAll() {
		_privRepository.deleteAll();
	}
}

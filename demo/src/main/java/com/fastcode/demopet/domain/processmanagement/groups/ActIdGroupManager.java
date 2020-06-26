package com.fastcode.demopet.domain.processmanagement.groups;

import com.fastcode.demopet.domain.irepository.IActIdGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActIdGroupManager implements IActIdGroupManager {
	@Autowired
	private IActIdGroupRepository _groupRepository;

	@Override
	public ActIdGroupEntity findByGroupId(String name) {
		return _groupRepository.findByGroupId(name);
	}

	@Override
	public void create(ActIdGroupEntity actIdGroup) {
		_groupRepository.save(actIdGroup);
	}

	@Override
	public void delete(ActIdGroupEntity actIdGroup) {
		_groupRepository.delete(actIdGroup);
	}

	@Override
	public void deleteAll() {
		_groupRepository.deleteAll();
	}
}

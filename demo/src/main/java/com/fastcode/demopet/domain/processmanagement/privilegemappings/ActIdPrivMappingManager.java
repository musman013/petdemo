package com.fastcode.demopet.domain.processmanagement.privilegemappings;

import com.fastcode.demopet.domain.irepository.IActIdPrivMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActIdPrivMappingManager implements IActIdPrivMappingManager {
	@Autowired
	private IActIdPrivMappingRepository _privMappingRepository;

	@Override
	public void create(ActIdPrivMappingEntity actIdPrivMapping) {
		_privMappingRepository.save(actIdPrivMapping);
	}

	@Override
	public ActIdPrivMappingEntity findByUserPrivilege(String userId, String privName) {
		return _privMappingRepository.findByUserPrivilege(userId, privName);
	}

	@Override
	public ActIdPrivMappingEntity findByGroupPrivilege(String groupId, String privName) {
		return _privMappingRepository.findByGroupPrivilege(groupId, privName);
	}

	@Override
	public void delete(ActIdPrivMappingEntity actIdPrivMapping) {
		_privMappingRepository.delete(actIdPrivMapping);
	}

	@Override
	public List<ActIdPrivMappingEntity> findAllByUserId(String id) {
		return _privMappingRepository.findAllByUserId(id);
	}
	@Override
	public List<ActIdPrivMappingEntity> findAllByGroupId(String id) {
		return _privMappingRepository.findAllByGroupId(id);
	}

	@Override
	public void update(ActIdPrivMappingEntity actIdPrivMapping) {
		_privMappingRepository.save(actIdPrivMapping);
	}

	@Override
	public void deleteAll() {
		_privMappingRepository.deleteAll();
	}
}

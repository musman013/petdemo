package com.fastcode.demopet.domain.processmanagement.memberships;

import com.fastcode.demopet.domain.irepository.IActIdMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActIdMembershipManager implements IActIdMembershipManager {
	
	@Autowired
	private IActIdMembershipRepository _membershipsRepository;
	
	@Override
	public void create(ActIdMembershipEntity actIdMembership) {
		_membershipsRepository.save(actIdMembership);
	}

	@Override
	public void update(ActIdMembershipEntity actIdMembership) {
		_membershipsRepository.save(actIdMembership);
	}

	@Override
	public void delete(ActIdMembershipEntity actIdMembership) {
		_membershipsRepository.delete(actIdMembership);
	}

	@Override
	public ActIdMembershipEntity findByUserGroupId(String userId, String groupId) {
		return _membershipsRepository.findByUserGroupId(userId, groupId);
	}

	@Override
	public List<ActIdMembershipEntity> findAllByUserId(String id) {
    	return _membershipsRepository.findAllByUserId(id);
    }

    @Override
    public List<ActIdMembershipEntity> findAllByGroupId(String groupId) {
        return _membershipsRepository.findAllByGroupId(groupId);
    }

    public void deleteAll() {
        _membershipsRepository.deleteAll();
    }
    
}

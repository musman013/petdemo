package com.fastcode.demopet.domain.processmanagement.groups;

public interface IActIdGroupManager {
	ActIdGroupEntity findByGroupId(String name);

	void create(ActIdGroupEntity actIdGroup);

	void delete(ActIdGroupEntity actIdGroup);

	void deleteAll();
}

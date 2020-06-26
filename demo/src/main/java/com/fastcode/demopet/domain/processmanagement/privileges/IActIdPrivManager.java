package com.fastcode.demopet.domain.processmanagement.privileges;

public interface IActIdPrivManager {

	void create(ActIdPrivEntity actIdPrivilege);

	ActIdPrivEntity findByName(String name);

	void delete(ActIdPrivEntity actIdPrivilege);

	void update(ActIdPrivEntity actIdPrivilege);

	void deleteAll();
}

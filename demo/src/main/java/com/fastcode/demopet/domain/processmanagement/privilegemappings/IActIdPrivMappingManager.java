package com.fastcode.demopet.domain.processmanagement.privilegemappings;

import java.util.List;

public interface IActIdPrivMappingManager {
void create(ActIdPrivMappingEntity actIdPrivMapping);

ActIdPrivMappingEntity findByUserPrivilege(String userId, String privName);

ActIdPrivMappingEntity findByGroupPrivilege(String groupId, String privName);

void delete(ActIdPrivMappingEntity actIdPrivMapping);

List<ActIdPrivMappingEntity> findAllByUserId(String id);

    List<ActIdPrivMappingEntity> findAllByGroupId(String id);

        void update(ActIdPrivMappingEntity actIdPrivMapping);

        void deleteAll();
        }

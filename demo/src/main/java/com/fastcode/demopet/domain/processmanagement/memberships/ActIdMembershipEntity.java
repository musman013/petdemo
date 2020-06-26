package com.fastcode.demopet.domain.processmanagement.memberships;

import com.fastcode.demopet.domain.processmanagement.groups.ActIdGroupEntity;
import com.fastcode.demopet.domain.processmanagement.users.ActIdUserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "act_id_membership")
@IdClass(MembershipId.class)
@Getter @Setter
@NoArgsConstructor
public class ActIdMembershipEntity {

	@ManyToOne
	@JoinColumn(name = "group_id_", insertable=false, updatable=false)
	private ActIdGroupEntity actIdGroup;

	@ManyToOne
	@JoinColumn(name = "user_id_", insertable=false, updatable=false)
	private ActIdUserEntity actIdUser;

	@Id
	@Column(name = "group_id_", nullable = false, length =64)
	private String groupId;

	@Id
	@Column(name = "user_id_", nullable = false, length =64)
	private String userId;
}


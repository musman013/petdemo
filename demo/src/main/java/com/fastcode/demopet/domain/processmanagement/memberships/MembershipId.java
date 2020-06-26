package com.fastcode.demopet.domain.processmanagement.memberships;

import java.io.Serializable;

public class MembershipId implements Serializable {
	
	private String groupId;
	private String userId;
	
	public MembershipId() {

	}
	
	public MembershipId(String groupId, String userId) {
		this.groupId = groupId;
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
		return true;
		if (obj == null)
		return false;
		if (getClass() != obj.getClass())
		return false;
		MembershipId other = (MembershipId) obj;
		if (groupId == null) {
		if (other.groupId != null)
		return false;
		} else if (!groupId.equals(other.groupId))
		return false;
		if (userId == null) {
		if (other.userId != null)
		return false;
		} else if (!userId.equals(other.userId))
		return false;
		return true;
	}
	
}

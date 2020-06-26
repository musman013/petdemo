package com.fastcode.demopet.domain.processmanagement.tokens;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_id_token")
@Getter @Setter
@NoArgsConstructor
public class ActIdTokenEntity implements Serializable {

	@Id
	@Column(name = "id_", nullable = false, length = 64)
	private String id;

	@Basic
	@Column(name = "rev_", nullable = true)
	private Long rev;

	@Basic
	@Column(name = "token_value_", nullable = true, length = 255)
	private String tokenValue;
	
	
	@Basic
	@Column(name = "token_date_", nullable = true)
	private Date tokenDate;

	@Basic
	@Column(name = "ip_address_", nullable = true, length = 255)
	private String ipAddress;

	@Basic
	@Column(name = "user_agent_", nullable = true, length = 255)
	private String userAgent;

	@Basic
	@Column(name = "user_id_", nullable = true, length = 255)
	private String userId;

	@Basic
	@Column(name = "token_data_", nullable = true, length = 2000)
	private String tokenData;

/*	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<>();
		persistentState.put("tokenValue", tokenValue);
		persistentState.put("tokenDate", tokenDate);
		persistentState.put("ipAddress", ipAddress);
		persistentState.put("userAgent", userAgent);
		persistentState.put("userId", userId);
		persistentState.put("tokenData", tokenData);

		return persistentState;
	}*/

// common methods //////////////////////////////////////////////////////////
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (!(o instanceof ActIdTokenEntity)) return false;
//		ActIdTokenEntity token = (ActIdTokenEntity) o;
//		return id != null && id.equals(token.id);
//	}
//	
//	@Override
//	public String toString() {
//		return "TokenEntity[tokenValue=" + tokenValue + ", userId=" + userId + "]";
//	}

}

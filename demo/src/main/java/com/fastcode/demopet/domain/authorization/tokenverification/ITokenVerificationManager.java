package com.fastcode.demopet.domain.authorization.tokenverification;

import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.model.TokenverificationEntity;

@Component
public interface ITokenVerificationManager {

	 TokenverificationEntity create(TokenverificationEntity entity);
	 
	 void delete(TokenverificationEntity entity);
	 
	 TokenverificationEntity findByTokenAndType(String token, String tokenType);
	 
	 TokenverificationEntity findByUserIdAndType(Long userId, String tokenType);
}

package com.fastcode.demopet.application.authorization.tokenverification;

import org.springframework.stereotype.Service;

import com.fastcode.demopet.domain.model.TokenverificationEntity;


@Service
public interface ITokenVerificationAppService {
	
	public TokenverificationEntity findByTokenAndType(String token, String type);
	public TokenverificationEntity findByUserIdAndType(Long userId, String type);
	public TokenverificationEntity generateToken(String type, Long userId);
	public void deleteToken(TokenverificationEntity entity);

}

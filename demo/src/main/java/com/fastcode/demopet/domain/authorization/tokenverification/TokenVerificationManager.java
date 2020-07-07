package com.fastcode.demopet.domain.authorization.tokenverification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.domain.irepository.ITokenVerificationRepository;
import com.fastcode.demopet.domain.model.TokenverificationEntity;


@Component
public class TokenVerificationManager implements ITokenVerificationManager {
	
	@Autowired
	private ITokenVerificationRepository _tokenRepository;
	
	public TokenverificationEntity findByTokenAndType(String token, String tokenType) {
		return  _tokenRepository.findByTokenAndTokenType(token, tokenType);
	}
	
	public TokenverificationEntity findByUserIdAndType(Long userId, String tokenType) {
		return  _tokenRepository.findByUserIdAndTokenType(userId, tokenType);
	}
	
	public TokenverificationEntity create(TokenverificationEntity entity) {
		return  _tokenRepository.save(entity);
	}
	
	public void delete(TokenverificationEntity entity) {
		 _tokenRepository.delete(entity);
	}
	
}

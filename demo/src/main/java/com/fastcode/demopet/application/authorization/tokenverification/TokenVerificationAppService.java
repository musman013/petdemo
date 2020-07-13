package com.fastcode.demopet.application.authorization.tokenverification;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fastcode.demopet.domain.authorization.tokenverification.ITokenVerificationManager;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.TokenverificationEntity;
import com.fastcode.demopet.domain.model.UserEntity;

@Service
public class TokenVerificationAppService implements ITokenVerificationAppService {
	
	@Autowired
	private ITokenVerificationManager _tokenManager;
	
	@Autowired
	private IUserManager _userManager;
	
	public static final long PASSWORD_TOKEN_EXPIRATION_TIME = 3_600_000; // 1 hour
	public static final long ACCOUNT_VERIFICATION_TOKEN_EXPIRATION_TIME = 86_400_000;

	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public TokenverificationEntity findByTokenAndType(String token, String type) {

		TokenverificationEntity foundToken = _tokenManager.findByTokenAndType(token, type);
		if (foundToken == null) {
			return null;
		}
	
		return  foundToken;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public TokenverificationEntity findByUserIdAndType(Long userId, String type) {

		TokenverificationEntity foundToken = _tokenManager.findByUserIdAndType(userId, type);
		if (foundToken == null) {
			return null;
		}
	
		return  foundToken;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public TokenverificationEntity generateToken(String type, Long userId)
	{
		UserEntity user = _userManager.findById(userId);
		TokenverificationEntity entity = new TokenverificationEntity();
		entity.setTokenType(type); 
		entity.setToken(UUID.randomUUID().toString());
		if(type.equalsIgnoreCase("password")) {
		entity.setExpirationTime(new Date(System.currentTimeMillis() + PASSWORD_TOKEN_EXPIRATION_TIME));
		}
		else if (type.equalsIgnoreCase("registration")) {
			entity.setExpirationTime(new Date(System.currentTimeMillis() + ACCOUNT_VERIFICATION_TOKEN_EXPIRATION_TIME));
		}
		entity.setId(user.getId());
		entity.setUser(user);
		return _tokenManager.create(entity);
		
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void deleteToken(TokenverificationEntity entity)
	{
         _tokenManager.delete(entity);
	}
}

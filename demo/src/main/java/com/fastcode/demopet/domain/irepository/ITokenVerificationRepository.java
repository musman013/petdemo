package com.fastcode.demopet.domain.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.fastcode.demopet.domain.model.TokenverificationEntity;
import com.fastcode.demopet.domain.model.TokenverificationId;


@Repository
public interface ITokenVerificationRepository extends JpaRepository<TokenverificationEntity, TokenverificationId>,QuerydslPredicateExecutor<TokenverificationEntity> {

//	@Query("select u from TokenVerificationEntity u where u.token = ?1 and u.tokenType = ?2")
	TokenverificationEntity findByTokenAndTokenType(String token, String tokenType);
	
	TokenverificationEntity findByUserIdAndTokenType(Long userId, String tokenType);
}

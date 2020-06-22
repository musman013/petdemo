package com.fastcode.demopet.security;

import com.fastcode.demopet.domain.irepository.IJwtRepository;
import com.fastcode.demopet.domain.model.JwtEntity;
import com.fastcode.demopet.domain.model.OwnersEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.domain.model.VetsEntity;
import com.fastcode.demopet.domain.owners.IOwnersManager;
import com.fastcode.demopet.domain.vets.IVetsManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.ApplicationContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private IUserManager _userManager;
    private IOwnersManager _ownerManager;
    private IVetsManager _vetManager;
    private IJwtRepository jwtRepo;
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
    	this._userManager = ctx.getBean(IUserManager.class);
    	this._ownerManager = ctx.getBean(IOwnersManager.class);
    	this._vetManager = ctx.getBean(IVetsManager.class);
		this.jwtRepo = ctx.getBean(IJwtRepository.class);
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            System.out.println("I am here ...");
            LoginUserInput creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginUserInput.class);
        UserEntity user = _userManager.findByUserName(creds.getUserName());
        if(user != null && user.getIsActive())
		{
			return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUserName(),creds.getPassword(),new ArrayList<>()));
		}
		else
			throw new InvalidCredentialsException("Invalid Credentials");
		} catch (IOException | InvalidCredentialsException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        Claims claims = Jwts.claims();
        String userName = "";
   
        if (auth != null) {
            if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                userName = ((User) auth.getPrincipal()).getUsername();
                claims.setSubject(userName);
            }
        }
        
        claims.put("scopes", (auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList())));
        UserEntity user = _userManager.findByUserName(userName);
        OwnersEntity owner = _ownerManager.findById(user.getId());
        VetsEntity vet = _vetManager.findById(user.getId());
        
        String role=null;

        if(owner !=null)
        {
        	role="owner";
        }
        if(vet !=null)
        {
        	role ="vet";
        }
        
        user.setLastLoginTime(new Date());
        claims.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME));
        claims.put("role" , role);
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();
                
        // Add the user and token to the JwtEntity table 
        JwtEntity jt = new JwtEntity(); 
        jt.setToken("Bearer "+ token); 
        jt.setUserName(userName); 
        jt.setRole(role);
        
        jwtRepo.save(jt); 
        
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.setContentType("application/json");

        PrintWriter out = res.getWriter();
        out.println("{");
        out.println("\"token\":" + "\"" + SecurityConstants.TOKEN_PREFIX + token + "\"");
     
        out.println("}");
        out.close();

    }

}

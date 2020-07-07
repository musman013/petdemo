package com.fastcode.demopet.restcontrollers;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastcode.demopet.application.authorization.tokenverification.TokenVerificationAppService;
import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.CreateUserInput;
import com.fastcode.demopet.application.authorization.user.dto.CreateUserOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserByNameOutput;
import com.fastcode.demopet.application.authorization.user.dto.FindUserWithAllFieldsByIdOutput;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.domain.model.TokenverificationEntity;
import com.fastcode.demopet.emailbuilder.application.mail.AsyncMailTrigger;
import com.fastcode.demopet.emailbuilder.application.mail.IEmailService;

@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private PasswordEncoder pEncoder;

	@Autowired 
	private TokenVerificationAppService _tokenAppService;

	@Autowired
	private UserAppService _userAppService;
	
	@Autowired
	public AsyncMailTrigger _asyncEmailTrigger;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private IEmailService _emailService;

	public static final long ACCOUNT_VERIFICATION_TOKEN_EXPIRATION_TIME = 86_400_000;


	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<HashMap<String,String>> registerUserAccount(@RequestBody CreateUserInput user, HttpServletRequest request) {

		FindUserByNameOutput foundUser = _userAppService.findByUserName(user.getUserName());

		if (foundUser != null) {
			logHelper.getLogger().error("There already exists a user with a name=%s", user.getUserName());
			throw new EntityExistsException(
					String.format("There already exists a user with a name=%s", user.getUserName()));
		}

		foundUser = _userAppService.findByEmailAddress(user.getEmailAddress());
		if (foundUser != null) {
			logHelper.getLogger().error("There already exists a user with a email=%s", user.getEmailAddress());
			throw new EntityExistsException(
					String.format("There already exists a user with a email=%s", user.getEmailAddress()));
		}

		user.setIsActive(false);
		user.setPassword(pEncoder.encode(user.getPassword()));

		CreateUserOutput output=_userAppService.create(user);
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("No record found")));


		TokenverificationEntity tokenEntity = _tokenAppService.generateToken("registration", output.getId());

		String appUrl = request.getScheme() + "://" + request.getServerName()+ ":" + request.getLocalPort() +"/register";
		System.out.println("App url " + appUrl);
		_asyncEmailTrigger.sendEmail(_emailService.buildVerifyRegistrationEmail(user.getEmailAddress(), appUrl, tokenEntity.getToken()));

		String msg = "Account verfication link has been sent to " + user.getEmailAddress();
		HashMap resultMap = new HashMap<String,String>();
		resultMap.put("message", msg);

		return new ResponseEntity(resultMap, HttpStatus.OK);

	}

	@RequestMapping(value = "/verifyEmail", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String,String>> verifyEmail(@RequestParam("token") final String token) {

		TokenverificationEntity tokenEntity = _tokenAppService.findByTokenAndType(token, "registration");

		Optional.ofNullable(tokenEntity).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid verification link.")));

		FindUserWithAllFieldsByIdOutput output = _userAppService.findWithAllFieldsById(tokenEntity.getId());
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid verification link.")));

		if(new Date().after(tokenEntity.getExpirationTime()))
		{
			_tokenAppService.deleteToken(tokenEntity);
			_userAppService.delete(output.getId());
			logHelper.getLogger().error("Token has expired, please register again");
			throw new EntityNotFoundException(
					String.format("Token has expired, please register again"));
		}

		output.setIsActive(true);
		_userAppService.updateUserData(output);
		_tokenAppService.deleteToken(tokenEntity);
		//	output.setPassword(pEncoder.encode(input.getPassword()));
		//	tokenEntity.setToken(null);


		String msg = "User Verified!";
		HashMap resultMap = new HashMap<String,String>();
		resultMap.put("message", msg);
		return new ResponseEntity(resultMap, HttpStatus.OK);


	}

}

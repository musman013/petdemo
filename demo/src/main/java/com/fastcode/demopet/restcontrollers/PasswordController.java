package com.fastcode.demopet.restcontrollers;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fastcode.demopet.application.authorization.user.UserAppService;
import com.fastcode.demopet.application.authorization.user.dto.ResetPasswordInput;
import com.fastcode.demopet.application.authorization.user.dto.UpdatePasswordInput;
import com.fastcode.demopet.emailbuilder.application.mail.IEmailService;
import com.fastcode.demopet.commons.logging.LoggingHelper;
import com.fastcode.demopet.domain.authorization.user.IUserManager;
import com.fastcode.demopet.domain.model.UserEntity;
import com.fastcode.demopet.security.JWTAppService;

@RestController
@RequestMapping("/password")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PasswordController {

	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private IUserManager _userManager;

	@Autowired
	private IEmailService _emailService;

	@Autowired
	private PasswordEncoder pEncoder;
	
	@Autowired
	private JWTAppService _jwtAppService;

	@Autowired
	private LoggingHelper logHelper;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String,String>> processForgotPassword(@RequestBody String email, HttpServletRequest request) throws InvalidInputException {
		
		if(email == null || !validate(email))
		{
			logHelper.getLogger().error("Email is not valid");
			throw new InvalidInputException("Email is not valid");
		}

		UserEntity user = _userAppService.savePasswordResetCode(email);
		Optional.ofNullable(user).orElseThrow(() -> new EntityNotFoundException(String.format("There does not exist a user with a email=%s", email)));

		String appUrl = request.getScheme() + "://" + request.getServerName();
		_emailService.sendEmail(_emailService.buildEmail(email, appUrl, user.getPasswordResetCode()));
		String msg = "A password reset link has been sent to " + email;
		HashMap resultMap = new HashMap<String,String>();
		resultMap.put("message", msg);
		return new ResponseEntity(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String,String>> setNewPassword(@RequestBody ResetPasswordInput input) {
		UserEntity output = _userAppService.findByPasswordResetCode(input.getToken());
		Optional.ofNullable(output).orElseThrow(() -> new EntityNotFoundException(String.format("Invalid password reset link.")));

		if(new Date().after(output.getPasswordTokenExpiration()))
		{
			logHelper.getLogger().error("Token has expired, please request a new password reset");
			throw new EntityNotFoundException(
					String.format("Token has expired, please request a new password reset"));
		}

		output.setPassword(pEncoder.encode(input.getPassword()));
		output.setPasswordResetCode(null);
		_userManager.update(output);
		_jwtAppService.deleteAllUserTokens(output.getUserName()); 
		String msg = "Password reset successfully !";
		HashMap resultMap = new HashMap<String,String>();
		resultMap.put("message", msg);
		return new ResponseEntity(resultMap, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<HashMap<String,String>> changePassword(@RequestBody UpdatePasswordInput input) throws InvalidInputException {
		UserEntity loggedInUser =  _userAppService.getUser();
		if(!pEncoder.matches(input.getOldPassword(), loggedInUser.getPassword()))
		{
			logHelper.getLogger().error("Invalid Old password");
			throw new InvalidInputException(
					String.format("Invalid Old password"));
		}
		if(pEncoder.matches(input.getNewPassword(), loggedInUser.getPassword()))
		{
			logHelper.getLogger().error("You cannot set prevoius password again");
			throw new InvalidInputException(
					String.format("You cannot set prevoius password again"));
		}

        loggedInUser.setPassword(pEncoder.encode(input.getNewPassword()));
		_userManager.update(loggedInUser);
		_jwtAppService.deleteAllUserTokens(loggedInUser.getUserName()); 
        String msg = "Password updated successfully !";
		HashMap resultMap = new HashMap<String,String>();
		resultMap.put("message", msg);
		return new ResponseEntity(resultMap, HttpStatus.OK);
	}


}
package com.fastcode.demo.application.authorization.user.dto;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateUserOutput {

    private Long id;
    private int accessFailedCount;
    private String authenticationSource;
    private String emailAddress;
    private String emailConfirmationCode;
    private String googleAuthenticatorKey;
    private Boolean isEmailConfirmed;
    private Boolean isLockoutEnabled;
    private Boolean isPhoneNumberConfirmed;
    private Boolean isTwoFactorEnabled;
    private Date lastLoginTime;
    private Date lockoutEndDateUtc;
    private String firstName;
    private Boolean isActive;
    private Boolean shouldChangePasswordOnNextLogin;
    private String passwordResetCode;
    private String phoneNumber;
    private Long profilePictureId;
    private String signInToken;
    private Date signInTokenExpireTimeUtc;
    private String lastName;
    private String userName;
    
}

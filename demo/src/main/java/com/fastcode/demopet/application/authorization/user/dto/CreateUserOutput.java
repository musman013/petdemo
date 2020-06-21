package com.fastcode.demopet.application.authorization.user.dto;

import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserOutput {

    private Long id;
    private int accessFailedCount;
    private String emailAddress;
    private String emailConfirmationCode;
    private Boolean isEmailConfirmed;
    private Boolean isLockoutEnabled;
    private Boolean isPhoneNumberConfirmed;
    private Date lastLoginTime;
    private Date lockoutEndDateUtc;
    private String firstName;
    private Boolean isActive;
    private String passwordResetCode;
    private Boolean shouldChangePasswordOnNextLogin;
    private String phoneNumber;
    private Long profilePictureId;
    private String signInToken;
    private Date signInTokenExpireTimeUtc;
    private String lastName;
    private String userName;
    
}

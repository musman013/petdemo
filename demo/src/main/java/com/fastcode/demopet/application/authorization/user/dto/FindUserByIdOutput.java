package com.fastcode.demopet.application.authorization.user.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindUserByIdOutput {

    private Long id;
    private int accessFailedCount;
    private String emailAddress;
    private Boolean isEmailConfirmed;
    private Boolean isLockoutEnabled;
    private Boolean isPhoneNumberConfirmed;
    private Date lastLoginTime;
    private Date lockoutEndDateUtc;
    private String firstName; 
    private String phoneNumber;
    private Long profilePictureId;
    private Boolean isActive;
    private Boolean shouldChangePasswordOnNextLogin;
    private String lastName;
    private String userName;
    private String authenticationSource;
    private String theme;
    private String language;
   
}

package com.fastcode.demo.application.authorization.userpermission.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetUserOutput {

  private Integer accessFailedCount;
  private String authenticationSource;
  private String emailAddress;
  private String emailConfirmationCode;
  private String firstName;
  private Long id;
  private Boolean isActive;
  private Boolean isEmailConfirmed;
  private Boolean isLockoutEnabled;
  private String isPhoneNumberConfirmed;
  private Date lastLoginTime;
  private String lastName;
  private Date lockoutEndDateUtc;
  private String password;
  private String passwordResetCode;
  private String phoneNumber;
  private Long profilePictureId;
  private Boolean twoFactorEnabled;
  private String userName;
  private Long UserpermissionPermissionId;
  private Long UserpermissionUserId;
  
  
}

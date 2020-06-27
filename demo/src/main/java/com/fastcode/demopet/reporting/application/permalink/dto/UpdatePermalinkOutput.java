package com.fastcode.demopet.reporting.application.permalink.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdatePermalinkOutput {

  private String authentication;
  private Boolean description;
  private UUID id;
  private Long refreshRate;
  private String rendering;
  private String resource;
  private Long resourceId;
  private Boolean toolbar;
  private Long userId;
  private String password;

}
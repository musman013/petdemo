package com.fastcode.demopet.reporting.application.permalink.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdatePermalinkInput {

  @Length(max = 30, message = "authentication must be less than 30 characters")
  private String authentication;
  private Boolean description;
  @NotNull(message = "id Should not be null")
//  @Length(max = 256, message = "id must be less than 256 characters")
  private UUID id;
  private Long refreshRate;
  @NotNull(message = "rendering Should not be null")
  @Length(max = 30, message = "rendering must be less than 30 characters")
  private String rendering;
  @NotNull(message = "resource Should not be null")
  @Length(max = 30, message = "resource must be less than 30 characters")
  private String resource;
  private Long resourceId;
  private Boolean toolbar;
  private Long userId;
  private Long version;

}

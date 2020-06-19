package com.fastcode.demo.application.authorization.user.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetRoleOutput {
    private Long id;
    private String displayName;
    private String name;
    private Long userId;
    private String userDescriptiveField;

}


package com.fastcode.demopet.scheduler.application.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindByJobInput {

    private Long id;
    private String jobName;
    private String jobGroup;
    private String jobClass;
    private Date startTime;
    private Date finishedTime;
    private String duration;
    private String jobStatus;
    private String jobMapData;

}

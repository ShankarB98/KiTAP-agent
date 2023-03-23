package com.kitap.agent.database.model.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class AgentDto {
    private String name;
    private String description;
    private String ipAddress;
    private Integer portNumber;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
    private String osName;
    private String osVersion;
    private String deviceType;
    private String cloudVendor;
    private String macAddress;
    private String hostName;
}

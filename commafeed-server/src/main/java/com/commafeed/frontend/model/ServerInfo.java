package com.commafeed.frontend.model;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;

@SuppressWarnings("serial")
@Schema(description = "Server infos")
@Data
public class ServerInfo implements Serializable {

	@Schema
	private String announcement;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String version;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private String gitCommit;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private boolean allowRegistrations;

	@Schema
	private String googleAnalyticsCode;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private boolean smtpEnabled;

	@Schema(requiredMode = RequiredMode.REQUIRED)
	private boolean demoAccountEnabled;

}

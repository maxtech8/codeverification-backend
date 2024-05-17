package org.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GetPhoneNumberRequest {
	@NotBlank
    @NotNull
    private Integer num;
	
	@NotBlank
    @NotNull
    private String projectId;
	
	@NotBlank
    @NotNull
    private String appName;
	
	public Integer getNum() {
		return num;
	}
	
	public String getProjectId() {
		return projectId;
	}
	
	public String getAppName() {
		return appName;
	}

}
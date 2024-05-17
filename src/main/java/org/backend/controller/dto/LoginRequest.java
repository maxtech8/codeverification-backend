package org.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginRequest {
	@NotBlank
    @NotNull
    private String email;
	
	@NotBlank
    @NotNull
    private String password;
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}

}

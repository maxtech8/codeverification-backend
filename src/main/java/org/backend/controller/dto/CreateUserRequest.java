package org.backend.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateUserRequest {
	@NotBlank
    @NotNull
    private String email;
	
	@NotBlank
    @NotNull
    private String password;
	
	@NotBlank
    @NotNull
    private String firstName;
	
	
	@NotBlank
    @NotNull
    private String lastName;
	
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

}

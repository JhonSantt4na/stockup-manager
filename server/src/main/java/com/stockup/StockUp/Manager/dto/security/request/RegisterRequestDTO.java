package com.stockup.StockUp.Manager.dto.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
	
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
	private String password;
}
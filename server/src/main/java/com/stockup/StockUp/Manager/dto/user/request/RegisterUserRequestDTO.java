package com.stockup.StockUp.Manager.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDTO {
	
	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50)
	private String username;
	
	@NotBlank(message = "Full name is required")
	@Size(min = 2, max = 100)
	private String fullName;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, max = 100)
	private String password;
}
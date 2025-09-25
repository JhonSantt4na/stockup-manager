package com.stockup.StockUp.Manager.dto.securityDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegisterDTO {
	@NotBlank
	private String username;
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
}

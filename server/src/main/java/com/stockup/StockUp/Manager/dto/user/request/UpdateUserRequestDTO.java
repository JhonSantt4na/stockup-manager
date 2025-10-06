package com.stockup.StockUp.Manager.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
	
	@Size(min = 2, max = 100)
	private String fullName;
	
	@Email
	private String email;
	
	@Size(min = 6, max = 100)
	private String password;
}
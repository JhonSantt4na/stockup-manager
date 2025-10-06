package com.stockup.StockUp.Manager.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
	@NotBlank(message = "Current password is required")
	private String currentPassword;
	
	@NotBlank(message = "New password is required")
	private String newPassword;
}
package com.stockup.StockUp.Manager.dto.Roles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AssignRolesDTO {
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotEmpty(message = "At least one role must be assigned")
	private List<String> roles;
}
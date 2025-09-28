package com.stockup.StockUp.Manager.dto.Roles;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleUpdateDTO {
	@NotBlank(message = "Old description is required")
	private String oldDescription;
	
	@NotBlank(message = "New description is required")
	private String newDescription;
}
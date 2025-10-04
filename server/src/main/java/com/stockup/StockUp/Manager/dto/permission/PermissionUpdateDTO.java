package com.stockup.StockUp.Manager.dto.permission;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionUpdateDTO {
	@NotBlank(message = "Old description is required")
	private String oldDescription;
	
	@NotBlank(message = "New description is required")
	private String newDescription;
}
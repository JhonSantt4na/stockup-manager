package com.stockup.StockUp.Manager.dto.roles.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleUpdateDTO {
	@NotBlank(message = "Old description is required")
	private String oldName;
	
	@NotBlank(message = "New description is required")
	private String newName;
	
	private Boolean enabled;
}
package com.stockup.StockUp.Manager.dto.permission.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PermissionCreateDTO {
	@NotBlank(message = "Role description is required")
	private String description;
}
package com.stockup.StockUp.Manager.dto.Roles;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleCreateDTO {
	@NotBlank(message = "Role description is required")
	private String description;
}
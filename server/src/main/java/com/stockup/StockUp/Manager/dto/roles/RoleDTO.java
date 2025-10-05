package com.stockup.StockUp.Manager.dto.roles;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
	private UUID id;
	@NotBlank(message = "Role name is required")
	private String name;
	private boolean enabled;
	private List<String> permissions;
}
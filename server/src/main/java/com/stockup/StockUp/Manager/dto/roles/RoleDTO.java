package com.stockup.StockUp.Manager.dto.roles;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
	@NotBlank(message = "Role name is required")
	private String name;
	private List<String> permissions;
}
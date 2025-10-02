package com.stockup.StockUp.Manager.dto.security.roles;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoleDTO {
	private String name;
	private List<String> roles;
}
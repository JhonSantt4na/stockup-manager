package com.stockup.StockUp.Manager.dto.user.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	
	private UUID id;
	private String username;
	private String fullName;
	private String email;
	private List<String> roles;
}
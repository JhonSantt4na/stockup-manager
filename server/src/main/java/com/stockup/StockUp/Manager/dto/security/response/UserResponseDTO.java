package com.stockup.StockUp.Manager.dto.security.response;

import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
	UUID id,
	String username,
	String fullName,
	String email,
	List<String> roles) {
}
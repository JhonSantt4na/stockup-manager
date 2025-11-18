package com.stockup.StockUp.Manager.dto.sales.Customer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CustomerResponseDTO(
	UUID id,
	String name,
	String cpfCnpj,
	String phone,
	String email,
	Boolean enabled,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	List<AddressDTO> addresses
) {}

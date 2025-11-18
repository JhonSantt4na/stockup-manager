package com.stockup.StockUp.Manager.dto.sales.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerSummaryDTO(
	UUID id,
	String name,
	String cpfCnpj,
	String phone,
	String email,
	Boolean enabled,
	LocalDateTime createdAt
) {}

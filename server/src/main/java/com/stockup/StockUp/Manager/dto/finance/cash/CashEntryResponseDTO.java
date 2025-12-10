package com.stockup.StockUp.Manager.dto.finance.cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashEntryResponseDTO(
	
	UUID id,
	String movementType,
	BigDecimal amount,
	String description,
	LocalDateTime createdAt

) {}
package com.stockup.StockUp.Manager.dto.finance.cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CashMovementResponseDTO(
	UUID id,
	UUID sessionId,
	BigDecimal amount,
	String type,
	String description,
	LocalDateTime timestamp
) {}
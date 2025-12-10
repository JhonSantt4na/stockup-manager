package com.stockup.StockUp.Manager.dto.finance.payable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PayableResponseDTO(
	UUID id,
	UUID supplierId,
	BigDecimal amount,
	LocalDate issueDate,
	LocalDate dueDate,
	String status,
	LocalDate paymentDate,
	String category,
	String description
) {}
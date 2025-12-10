package com.stockup.StockUp.Manager.dto.finance.cash;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CashRegisterResponseDTO(
	
	UUID id,
	LocalDateTime openedAt,
	LocalDateTime closedAt,
	BigDecimal openingAmount,
	BigDecimal closingAmount,
	BigDecimal systemExpectedAmount,
	BigDecimal differenceAmount,
	String status,
	UUID operatorOpenId,
	UUID operatorCloseId,
	List<CashEntryResponseDTO> entries

) {}
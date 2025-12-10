package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ICashMovementService {
	
	CashMovementResponseDTO create(CashMovementRequestDTO dto);
	
	CashMovementResponseDTO findById(UUID id);
	
	List<CashMovementResponseDTO> findBySession(UUID sessionId);
	
	void delete(UUID id);
}
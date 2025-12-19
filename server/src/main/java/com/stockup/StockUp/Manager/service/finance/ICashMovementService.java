package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ICashMovementService {
	
	CashMovementResponseDTO createCashMovement(CashMovementRequestDTO dto);
	
	CashMovementResponseDTO findCashMovementById(UUID id);
	
	List<CashMovementResponseDTO> findCashMovementBySession(UUID sessionId);
	
	void deleteCashMovement(UUID id);
}
package com.stockup.StockUp.Manager.service.finance;

import java.util.List;
import java.util.UUID;

public interface ICashMovementService {
	
	CashMovementResponseDTO create(CashMovementRequestDTO dto);
	
	CashMovementResponseDTO findById(UUID id);
	
	List<CashMovementResponseDTO> findBySession(UUID sessionId);
	
	List<CashMovementResponseDTO> findByType(String type);
	
	void delete(UUID id);
}
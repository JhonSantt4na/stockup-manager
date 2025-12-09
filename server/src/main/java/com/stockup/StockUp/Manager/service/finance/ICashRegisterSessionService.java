package com.stockup.StockUp.Manager.service.finance;

import java.util.List;
import java.util.UUID;

public interface ICashRegisterSessionService {
	
	CashRegisterSessionResponseDTO openSession(CashRegisterSessionRequestDTO dto);
	
	CashRegisterSessionResponseDTO closeSession(UUID sessionId, UUID userId);
	
	CashRegisterSessionResponseDTO suspendSession(UUID sessionId);
	
	CashRegisterSessionResponseDTO findById(UUID id);
	
	List<CashRegisterSessionResponseDTO> findByRegister(UUID registerId);
	
	List<CashRegisterSessionResponseDTO> findByUser(UUID userId);
}
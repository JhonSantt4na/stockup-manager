package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.payments.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashEntryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ICashEntryService {
	
	CashEntryResponseDTO create(CashEntryRequestDTO dto);
	
	CashEntryResponseDTO findById(UUID id);
	
	List<CashEntryResponseDTO> findByCashRegister(UUID cashRegisterId);
	
	void delete(UUID id);
}
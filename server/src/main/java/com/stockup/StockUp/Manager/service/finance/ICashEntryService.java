package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICashEntryService {
	
	CashEntryResponseDTO create(CashEntryRequestDTO dto);
	CashEntryResponseDTO update(UUID id, CashEntryRequestDTO dto);
	CashEntryResponseDTO findById(UUID id);
	Page<CashEntryResponseDTO> findAll(Pageable pageable);
	void delete(UUID id);
}
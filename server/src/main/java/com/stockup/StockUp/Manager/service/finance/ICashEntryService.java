package com.stockup.StockUp.Manager.service.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICashEntryService {
	CashEntryResponseDTO createCashEntry(CashEntryRequestDTO dto);
	CashEntryResponseDTO updateCashEntry(UUID id, CashEntryRequestDTO dto);
	CashEntryResponseDTO findCashEntryById(UUID id);
	Page<CashEntry> findAllCashEntry(Pageable pageable);
	void deleteCashEntry(UUID id);
}
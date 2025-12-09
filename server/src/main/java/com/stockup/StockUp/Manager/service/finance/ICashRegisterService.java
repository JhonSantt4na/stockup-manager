package com.stockup.StockUp.Manager.service.finance;

import java.util.UUID;

import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterCloseRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterOpenRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICashRegisterService {
	
	CashRegisterResponseDTO openRegister(CashRegisterOpenRequestDTO dto);
	
	CashRegisterResponseDTO closeRegister(UUID cashRegisterId, CashRegisterCloseRequestDTO dto);
	
	CashRegisterResponseDTO findById(UUID id);
	
	CashRegisterResponseDTO findByIdentifier(String identifier);
	
	Page<CashRegisterResponseDTO> list(Pageable pageable);
}
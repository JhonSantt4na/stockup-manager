package com.stockup.StockUp.Manager.service.finance;

import java.util.UUID;

import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterCloseRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterOpenRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICashRegisterService {
	
	CashRegisterResponseDTO openRegister(CashRegisterOpenRequestDTO dto);
	CashRegisterResponseDTO closeRegister(UUID registerId, CashRegisterCloseRequestDTO dto);
	CashRegisterResponseDTO findById(UUID id);
	Page<CashRegisterResponseDTO> findAll(Pageable pageable);
}
package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterCloseRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterOpenRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashRegisterResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashRegisterMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterRepository;
import com.stockup.StockUp.Manager.service.finance.ICashRegisterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashRegisterService implements ICashRegisterService {
	
	private static final Logger logger = LoggerFactory.getLogger(CashRegisterService.class);
	private final CashRegisterRepository cashRegisterRepository;
	private final CashRegisterMapper mapper;
	
	@Override
	@Transactional
	public CashRegisterResponseDTO openCashRegister(CashRegisterOpenRequestDTO dto) {
		logger.info("Abrindo caixa com openingAmount={}", dto.openingAmount());
		
		CashRegister register = new CashRegister();
		register.setOpenedAt(LocalDateTime.now());
		register.setIdentifier(UUID.randomUUID().toString());
		register.setOpeningAmount(dto.openingAmount());
		register.setStatus(/*presumo enum*/ null); // setar CashStatus.OPEN conforme seu Enum
		// operatorOpenId deve vir do contexto (getCurrentUser) -- aqui deixei null por exemplo
		CashRegister saved = cashRegisterRepository.save(register);
		
		AuditLogger.log("CASH_REGISTER_OPEN", null, "SUCCESS", "Caixa aberto: " + saved.getId());
		return mapper.toResponse(saved);
	}
	
	@Override
	@Transactional
	public CashRegisterResponseDTO closeCashRegister(UUID registerId, CashRegisterCloseRequestDTO dto) {
		CashRegister register = cashRegisterRepository.findById(registerId)
			.orElseThrow(() -> new IllegalArgumentException("Caixa não encontrado: " + registerId));
		
		register.setClosedAt(LocalDateTime.now());
		register.setClosingAmount(dto.closingAmount());
		// calcular systemExpectedAmount / differenceAmount se necessário
		register.setStatus(/* CashStatus.CLOSED */ null);
		
		CashRegister saved = cashRegisterRepository.save(register);
		AuditLogger.log("CASH_REGISTER_CLOSE", null, "SUCCESS", "Caixa fechado: " + registerId);
		return mapper.toResponse(saved);
	}
	
	@Override
	public CashRegisterResponseDTO findCashRegisterById(UUID id) {
		CashRegister r = cashRegisterRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Caixa não encontrado: " + id));
		return mapper.toResponse(r);
	}
	
	@Override
	public Page<CashRegisterResponseDTO> findAllCashRegister(Pageable pageable) {
		Page<CashRegister> page = cashRegisterRepository.findAll(pageable);
		return page.map(mapper::toResponse);
	}
}
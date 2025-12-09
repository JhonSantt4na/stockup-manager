package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterCloseRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterOpenRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashRegisterMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import com.stockup.StockUp.Manager.Enums.finance.CashStatus;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterRepository;
import com.stockup.StockUp.Manager.service.finance.ICashRegisterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CashRegisterService implements ICashRegisterService {
	
	private final CashRegisterRepository cashRegisterRepository;
	private final CashRegisterMapper cashRegisterMapper;
	
	@Override
	public CashRegisterResponseDTO openRegister(CashRegisterOpenRequestDTO dto) {
		
		boolean existsIdentifier = cashRegisterRepository.existsByIdentifier(dto.getIdentifier());
		
		if (existsIdentifier) {
			throw new IllegalStateException("Já existe um caixa com este identificador.");
		}
		
		CashRegister cashRegister = new CashRegister();
		
		cashRegister.setIdentifier(dto.getIdentifier());
		cashRegister.setOpenedAt(LocalDateTime.now());
		cashRegister.setOpeningAmount(dto.getOpeningAmount());
		cashRegister.setOperatorOpenId(dto.getOperatorOpenId());
		cashRegister.setStatus(CashStatus.OPEN);
		
		cashRegisterRepository.save(cashRegister);
		
		return cashRegisterMapper.toResponse(cashRegister);
	}
	
	@Override
	public CashRegisterResponseDTO closeRegister(UUID id, CashRegisterCloseRequestDTO dto) {
		
		CashRegister cashRegister = cashRegisterRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado."));
		
		if (cashRegister.getStatus() != CashStatus.OPEN) {
			throw new IllegalStateException("O caixa não está aberto para ser fechado.");
		}
		
		cashRegister.setClosedAt(LocalDateTime.now());
		cashRegister.setClosingAmount(dto.getClosingAmount());
		cashRegister.setOperatorCloseId(dto.getOperatorCloseId());
		cashRegister.setStatus(CashStatus.CLOSED);
		
		BigDecimal systemExpected = dto.getSystemExpectedAmount();
		cashRegister.setSystemExpectedAmount(systemExpected);
		
		BigDecimal difference = dto.getClosingAmount().subtract(systemExpected);
		cashRegister.setDifferenceAmount(difference);
		
		cashRegisterRepository.save(cashRegister);
		
		return cashRegisterMapper.toResponse(cashRegister);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CashRegisterResponseDTO findById(UUID id) {
		CashRegister cashRegister = cashRegisterRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado."));
		return cashRegisterMapper.toResponse(cashRegister);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CashRegisterResponseDTO findByIdentifier(String identifier) {
		CashRegister cashRegister = cashRegisterRepository.findByIdentifier(identifier)
			.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado pelo identificador."));
		return cashRegisterMapper.toResponse(cashRegister);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<CashRegisterResponseDTO> list(Pageable pageable) {
		return cashRegisterRepository.findAll(pageable)
			.map(cashRegisterMapper::toResponse);
	}
}

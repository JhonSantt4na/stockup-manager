package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.dto.payments.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashEntryMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;

import com.stockup.StockUp.Manager.repository.finance.CashRegisterRepository;
import com.stockup.StockUp.Manager.service.finance.ICashEntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CashEntryService implements ICashEntryService {
	
	private final CashEntryRepository cashEntryRepository;
	private final CashRegisterRepository cashRegisterRepository;
	private final CashEntryMapper cashEntryMapper;
	
	@Override
	public CashEntryResponseDTO create(CashEntryRequestDTO dto) {
		CashRegister register = cashRegisterRepository.findById(dto.getCashRegisterId())
			.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado."));
		
		CashEntry entry = cashEntryMapper.toEntity(dto);
		entry.setCashRegister(register);
		
		cashEntryRepository.save(entry);
		return cashEntryMapper.toResponse(entry);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CashEntryResponseDTO findById(UUID id) {
		CashEntry entry = cashEntryRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Entrada de caixa não encontrada."));
		return cashEntryMapper.toResponse(entry);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CashEntryResponseDTO> findByCashRegister(UUID cashRegisterId) {
		return cashEntryRepository.findByCashRegisterId(cashRegisterId)
			.stream()
			.map(cashEntryMapper::toResponse)
			.toList();
	}
	
	@Override
	public void delete(UUID id) {
		if (!cashEntryRepository.existsById(id)) {
			throw new EntityNotFoundException("Entrada de caixa não encontrada.");
		}
		cashEntryRepository.deleteById(id);
	}
}

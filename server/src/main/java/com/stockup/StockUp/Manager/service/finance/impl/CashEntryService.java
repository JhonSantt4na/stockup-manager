package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashEntryMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import com.stockup.StockUp.Manager.repository.finance.CashEntryRepository;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterRepository;
import com.stockup.StockUp.Manager.service.finance.ICashEntryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashEntryService implements ICashEntryService {
	
	private static final Logger logger = LoggerFactory.getLogger(CashEntryService.class);
	private final CashRegisterRepository cashRegisterRepository;
	private final CashEntryMapper mapper;
	private final CashEntryRepository cashEntryRepository;
	
	@Override
	@Transactional
	public CashEntryResponseDTO create(CashEntryRequestDTO dto) {
		logger.info("Criando CashEntry para caixaId={}", dto.cashRegisterId());
		CashRegister cashRegister = cashRegisterRepository.findById(dto.cashRegisterId())
			.orElseThrow(() -> new IllegalArgumentException("Caixa não encontrado: " + dto.cashRegisterId()));
		
		CashEntry entity = mapper.toEntity(dto);
		entity.setCashRegister(cashRegister);
		
		CashEntry saved = cashEntryRepository.save(entity);
		
		AuditLogger.log("CASH_ENTRY_CREATE", /*getCurrentUser()*/ null, "SUCCESS",
			"CashEntry criado: " + saved.getId());
		
		return mapper.toResponse(saved);
	}
	
	@Override
	@Transactional
	public CashEntryResponseDTO update(UUID id, CashEntryRequestDTO dto) {
		CashEntry existing = cashEntryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("CashEntry não encontrado: " + id));
		
		mapper.updateEntityFromDTO(dto, existing);
		CashEntry updated = cashEntryRepository.save(existing);
		
		AuditLogger.log("CASH_ENTRY_UPDATE", null, "SUCCESS", "CashEntry atualizado: " + id);
		return mapper.toResponse(updated);
	}
	
	@Override
	public CashEntryResponseDTO findById(UUID id) {
		CashEntry e = cashEntryRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("CashEntry não encontrado: " + id));
		return mapper.toResponse(e);
	}
	
	@Override
	public Page<CashEntryResponseDTO> findAll(Pageable pageable) {
		Page<CashEntry> page = cashEntryRepository.findAll(pageable);
		return page.map(mapper::toResponse);
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		if (!cashEntryRepository.existsById(id)) {
			throw new IllegalArgumentException("CashEntry não encontrado: " + id);
		}
		cashEntryRepository.deleteById(id);
		AuditLogger.log("CASH_ENTRY_DELETE", null, "SUCCESS", "CashEntry excluído: " + id);
	}
}
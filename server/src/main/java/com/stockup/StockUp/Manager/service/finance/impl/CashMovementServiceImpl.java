package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashMovementMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashMovement;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegisterSession;
import com.stockup.StockUp.Manager.repository.finance.CashMovementRepository;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterSessionRepository;
import com.stockup.StockUp.Manager.service.finance.ICashMovementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashMovementServiceImpl implements ICashMovementService {
	
	private final CashMovementRepository repository;
	private final CashRegisterSessionRepository sessionRepository;
	private final CashMovementMapper mapper;
	
	@Override
	@Transactional
	public CashMovementResponseDTO create(CashMovementRequestDTO dto) {
		CashRegisterSession session = sessionRepository.findById(dto.sessionId())
			.orElseThrow(() -> new EntityNotFoundException("Sessão de caixa não encontrada."));
		
		CashMovement entity = mapper.toEntity(dto);
		entity.setSession(session);
		
		repository.save(entity);
		return mapper.toDTO(entity);
	}
	
	@Override
	public CashMovementResponseDTO findById(UUID id) {
		CashMovement entity = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada."));
		return mapper.toDTO(entity);
	}
	
	@Override
	public List<CashMovementResponseDTO> findBySession(UUID sessionId) {
		return repository.findBySessionId(sessionId)
			.stream()
			.map(mapper::toDTO)
			.toList();
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		CashMovement entity = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada."));
		repository.delete(entity);
	}
}

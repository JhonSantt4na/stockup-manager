package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.model.finance.cash.CashMovement;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegisterSession;
import com.stockup.StockUp.Manager.repository.finance.CashMovementRepository;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterSessionRepository;
import com.stockup.StockUp.Manager.service.finance.ICashMovementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CashMovementService implements ICashMovementService {
	
	private final CashMovementRepository cashMovementRepository;
	private final CashRegisterSessionRepository cashRegisterSessionRepository;
	private final CashMovementMapper cashMovementMapper;
	
	@Override
	public CashMovementResponseDTO create(CashMovementRequestDTO dto) {
		
		CashRegisterSession session = cashRegisterSessionRepository.findById(dto.getSessionId())
			.orElseThrow(() -> new EntityNotFoundException("Sessão de caixa não encontrada."));
		
		if (!session.getStatus().name().equals("OPEN")) {
			throw new IllegalStateException("Não é possível registrar movimento em sessão fechada / suspensa.");
		}
		
		CashMovement movement = cashMovementMapper.toEntity(dto);
		movement.setSession(session);
		movement.setTimestamp(LocalDateTime.now());
		
		cashMovementRepository.save(movement);
		
		return cashMovementMapper.toResponse(movement);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CashMovementResponseDTO findById(UUID id) {
		CashMovement movement = cashMovementRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada."));
		return cashMovementMapper.toResponse(movement);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CashMovementResponseDTO> findBySession(UUID sessionId) {
		return cashMovementRepository.findBySessionId(sessionId)
			.stream()
			.map(cashMovementMapper::toResponse)
			.toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CashMovementResponseDTO> findByType(String type) {
		return cashMovementRepository.findByType(type)
			.stream()
			.map(cashMovementMapper::toResponse)
			.toList();
	}
	
	@Override
	public void delete(UUID id) {
		if (!cashMovementRepository.existsById(id)) {
			throw new EntityNotFoundException("Movimentação não encontrada.");
		}
		cashMovementRepository.deleteById(id);
	}
}

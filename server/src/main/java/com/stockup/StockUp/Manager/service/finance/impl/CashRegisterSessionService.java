package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegisterSession;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegisterSession.SessionStatus;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterRepository;
import com.stockup.StockUp.Manager.repository.finance.CashRegisterSessionRepository;
import com.stockup.StockUp.Manager.service.finance.ICashRegisterSessionService;
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
public class CashRegisterSessionService implements ICashRegisterSessionService {
	
	private final CashRegisterRepository cashRegisterRepository;
	private final CashRegisterSessionRepository sessionRepository;
	private final CashRegisterSessionMapper sessionMapper;
	
	@Override
	public CashRegisterSessionResponseDTO openSession(CashRegisterSessionRequestDTO dto) {
		
		CashRegister cashRegister = cashRegisterRepository.findById(dto.getCashRegisterId())
			.orElseThrow(() -> new EntityNotFoundException("Caixa não encontrado."));
		
		boolean existsOpenSession = sessionRepository.existsByCashRegisterIdAndStatus(
			cashRegister.getId(),
			SessionStatus.OPEN
		);
		
		if (existsOpenSession) {
			throw new IllegalStateException("Já existe uma sessão aberta para este caixa.");
		}
		
		CashRegisterSession session = CashRegisterSession.builder()
			.cashRegister(cashRegister)
			.openedAt(LocalDateTime.now())
			.openedByUserId(dto.getUserId())
			.openingAmount(dto.getOpeningAmount())
			.status(SessionStatus.OPEN)
			.build();
		
		sessionRepository.save(session);
		
		return sessionMapper.toResponse(session);
	}
	
	@Override
	public CashRegisterSessionResponseDTO closeSession(UUID sessionId, UUID userId) {
		
		CashRegisterSession session = sessionRepository.findById(sessionId)
			.orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada."));
		
		if (session.getStatus() != SessionStatus.OPEN) {
			throw new IllegalStateException("A sessão não está aberta para ser fechada.");
		}
		
		session.setStatus(SessionStatus.CLOSED);
		session.setClosedAt(LocalDateTime.now());
		session.setClosedByUserId(userId);
		
		sessionRepository.save(session);
		
		return sessionMapper.toResponse(session);
	}
	
	@Override
	public CashRegisterSessionResponseDTO suspendSession(UUID sessionId) {
		
		CashRegisterSession session = sessionRepository.findById(sessionId)
			.orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada."));
		
		if (session.getStatus() != SessionStatus.OPEN) {
			throw new IllegalStateException("Apenas sessões abertas podem ser suspensas.");
		}
		
		session.setStatus(SessionStatus.SUSPENDED);
		
		sessionRepository.save(session);
		
		return sessionMapper.toResponse(session);
	}
	
	@Override
	@Transactional(readOnly = true)
	public CashRegisterSessionResponseDTO findById(UUID id) {
		
		CashRegisterSession session = sessionRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Sessão não encontrada."));
		
		return sessionMapper.toResponse(session);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CashRegisterSessionResponseDTO> findByRegister(UUID registerId) {
		
		List<CashRegisterSession> sessions =
			sessionRepository.findByCashRegisterId(registerId);
		
		return sessionMapper.toResponseList(sessions);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<CashRegisterSessionResponseDTO> findByUser(UUID userId) {
		
		List<CashRegisterSession> sessions =
			sessionRepository.findByOpenedByUserId(userId);
		
		return sessionMapper.toResponseList(sessions);
	}
}

package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.PayableMapper;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import com.stockup.StockUp.Manager.repository.finance.PayableRepository;
import com.stockup.StockUp.Manager.service.finance.IPayableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayableService implements IPayableService {
	
	private static final Logger logger = LoggerFactory.getLogger(PayableService.class);
	private final PayableRepository repository;
	private final PayableMapper mapper;
	
	@Override
	@Transactional
	public PayableResponseDTO createPayable(PayableRequestDTO dto) {
		logger.info("Criando payable para supplierId={}", dto.supplierId());
		Payable entity = mapper.toEntity(dto);
		// ajustar provider, payment, installments, status conforme regras
		Payable saved = repository.save(entity);
		AuditLogger.log("PAYABLE_CREATE", null, "SUCCESS", "Payable criado: " + saved.getId());
		return mapper.toResponse(saved);
	}
	
	@Override
	@Transactional
	public PayableResponseDTO updatePayable(UUID id, PayableRequestDTO dto) {
		Payable existing = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payable não encontrado: " + id));
		mapper.updateEntityFromDTO(dto, existing);
		Payable updated = repository.save(existing);
		AuditLogger.log("PAYABLE_UPDATE", null, "SUCCESS", "Payable atualizado: " + id);
		return mapper.toResponse(updated);
	}
	
	@Override
	public PayableResponseDTO findPayableById(UUID id) {
		Payable p = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Payable não encontrado: " + id));
		return mapper.toResponse(p);
	}
	
	@Override
	public Page<PayableResponseDTO> findAllPayable(Pageable pageable) {
		Page<Payable> page = repository.findAll(pageable);
		return page.map(mapper::toResponse);
	}
	
	@Override
	@Transactional
	public void deletePayable(UUID id) {
		if (!repository.existsById(id)) throw new IllegalArgumentException("Payable não encontrado: " + id);
		repository.deleteById(id);
		AuditLogger.log("PAYABLE_DELETE", null, "SUCCESS", "Payable excluído: " + id);
	}
	
	@Override
	public List<PayableResponseDTO> findPayableByPayment(UUID paymentId) {
		return List.of();
	}
	
	@Override
	public List<PayableResponseDTO> findPayableByStatus(String status) {
		return List.of();
	}
}
package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.PaymentMapper;
import com.stockup.StockUp.Manager.model.finance.payments.Payment;
import com.stockup.StockUp.Manager.repository.finance.PaymentMethodRepository;
import com.stockup.StockUp.Manager.repository.finance.PaymentRepository;
import com.stockup.StockUp.Manager.service.finance.IPaymentService;
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
public class PaymentService implements IPaymentService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	private final PaymentRepository repository;
	private final PaymentMethodRepository methodRepository;
	private final PaymentMapper mapper;
	
	@Override
	@Transactional
	public PaymentResponseDTO create(PaymentRequestDTO dto) {
		logger.info("Criando pagamento para referenceId={}", dto.referenceId());
		
		// validar método
		var method = methodRepository.findById(dto.paymentMethodId())
			.orElseThrow(() -> new IllegalArgumentException("Método de pagamento não encontrado: " + dto.paymentMethodId()));
		
		Payment entity = mapper.toEntity(dto);
		entity.setMethod(method);
		// definir status inicial
		Payment saved = repository.save(entity);
		
		AuditLogger.log("PAYMENT_CREATE", null, "SUCCESS", "Payment criado: " + saved.getId());
		return mapper.toResponse(saved);
	}
	
	@Override
	@Transactional
	public PaymentResponseDTO update(UUID id, PaymentRequestDTO dto) {
		Payment existing = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado: " + id));
		mapper.updateEntityFromDTO(dto, existing);
		var method = methodRepository.findById(dto.paymentMethodId()).orElseThrow(() -> new IllegalArgumentException("Método de pagamento não encontrado"));
		existing.setMethod(method);
		Payment updated = repository.save(existing);
		AuditLogger.log("PAYMENT_UPDATE", null, "SUCCESS", "Payment atualizado: " + id);
		return mapper.toResponse(updated);
	}
	
	@Override
	public PaymentResponseDTO findById(UUID id) {
		Payment p = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado: " + id));
		return mapper.toResponse(p);
	}
	
	@Override
	public Page<PaymentResponseDTO> findAll(Pageable pageable) {
		Page<Payment> page = repository.findAll(pageable);
		return page.map(mapper::toResponse);
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		if (!repository.existsById(id)) throw new IllegalArgumentException("Pagamento não encontrado: " + id);
		repository.deleteById(id);
		AuditLogger.log("PAYMENT_DELETE", null, "SUCCESS", "Payment excluído: " + id);
	}
	
	@Override
	public PaymentResponseDTO updateStatus(UUID id, String status) {
		return null;
	}
	
	@Override
	public List<PaymentResponseDTO> findByOrder(UUID orderId) {
		return List.of();
	}
	
	@Override
	public List<PaymentResponseDTO> findByStatus(String status) {
		return List.of();
	}
}
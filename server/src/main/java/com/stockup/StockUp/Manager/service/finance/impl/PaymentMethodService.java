package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.PaymentMethodMapper;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentMethod;
import com.stockup.StockUp.Manager.repository.finance.PaymentMethodRepository;
import com.stockup.StockUp.Manager.service.finance.IPaymentMethodService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements IPaymentMethodService {
	
	private static final Logger logger = LoggerFactory.getLogger(PaymentMethodService.class);
	private final PaymentMethodRepository repository;
	private final PaymentMethodMapper mapper;
	
	@Override
	@Transactional
	public PaymentMethodResponseDTO create(PaymentMethodRequestDTO dto) {
		logger.info("Criando PaymentMethod name={}", dto.name());
		// checar duplicidade por code/nome se necessário
		PaymentMethod entity = mapper.toEntity(dto);
		// entity.setCode(...) // gerar code se necessário
		PaymentMethod saved = repository.save(entity);
		AuditLogger.log("PAYMENT_METHOD_CREATE", null, "SUCCESS", "PaymentMethod criado: " + saved.getId());
		return mapper.toResponse(saved);
	}
	
	@Override
	@Transactional
	public PaymentMethodResponseDTO update(UUID id, PaymentMethodRequestDTO dto) {
		PaymentMethod existing = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("PaymentMethod não encontrado: " + id));
		mapper.updateEntityFromDTO(dto, existing);
		PaymentMethod updated = repository.save(existing);
		AuditLogger.log("PAYMENT_METHOD_UPDATE", null, "SUCCESS", "PaymentMethod atualizado: " + id);
		return mapper.toResponse(updated);
	}
	
	@Override
	public PaymentMethodResponseDTO findById(UUID id) {
		PaymentMethod pm = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("PaymentMethod não encontrado: " + id));
		return mapper.toResponse(pm);
	}
	
	@Override
	public Page<PaymentMethodResponseDTO> findAll(Pageable pageable) {
		Page<PaymentMethod> page = repository.findAll(pageable);
		return page.map(mapper::toResponse);
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		if (!repository.existsById(id)) throw new IllegalArgumentException("PaymentMethod não encontrado: " + id);
		repository.deleteById(id);
		AuditLogger.log("PAYMENT_METHOD_DELETE", null, "SUCCESS", "PaymentMethod excluído: " + id);
	}
	
	@Override
	public void activate(UUID id) {
	
	}
	
	@Override
	public void deactivate(UUID id) {
	
	}
}
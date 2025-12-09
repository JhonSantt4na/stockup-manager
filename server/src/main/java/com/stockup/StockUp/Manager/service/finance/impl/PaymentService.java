package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.mapper.finance.PaymentMapper;
import com.stockup.StockUp.Manager.model.finance.payments.Payment;
import com.stockup.StockUp.Manager.repository.finance.PaymentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentService {
	
	private final PaymentRepository repository;
	private final PaymentMapper mapper;
	
	@Override
	public PaymentDTO create(PaymentCreateDTO dto) {
		Payment entity = mapper.toEntity(dto);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public PaymentDTO update(UUID id, PaymentUpdateDTO dto) {
		Payment entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Pagamento não encontrado."));
		
		mapper.updateFromDto(dto, entity);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public void delete(UUID id) {
		Payment entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Pagamento não encontrado."));
		
		repository.delete(entity);
	}
	
	@Override
	public PaymentDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toDto)
			.orElseThrow(() -> new NotFoundException("Pagamento não encontrado."));
	}
	
	@Override
	public Page<PaymentDTO> findAll(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toDto);
	}
}

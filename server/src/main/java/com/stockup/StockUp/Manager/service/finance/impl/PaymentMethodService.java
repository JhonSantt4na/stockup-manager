package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.mapper.finance.PaymentMethodMapper;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentMethod;
import com.stockup.StockUp.Manager.repository.finance.PaymentMethodRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements PaymentMethodService {
	
	private final PaymentMethodRepository repository;
	private final PaymentMethodMapper mapper;
	
	@Override
	public PaymentMethodDTO create(PaymentMethodCreateDTO dto) {
		PaymentMethod entity = mapper.toEntity(dto);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public PaymentMethodDTO update(UUID id, PaymentMethodUpdateDTO dto) {
		PaymentMethod entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Método de pagamento não encontrado."));
		
		mapper.updateFromDto(dto, entity);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public void delete(UUID id) {
		PaymentMethod entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Método de pagamento não encontrado."));
		
		repository.delete(entity);
	}
	
	@Override
	public PaymentMethodDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toDto)
			.orElseThrow(() -> new NotFoundException("Método de pagamento não encontrado."));
	}
	
	@Override
	public Page<PaymentMethodDTO> findAll(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toDto);
	}
}

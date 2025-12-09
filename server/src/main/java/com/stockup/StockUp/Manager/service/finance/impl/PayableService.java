package com.stockup.StockUp.Manager.service.finance.impl;

import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.mapper.finance.PayableMapper;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import com.stockup.StockUp.Manager.repository.finance.PayableRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayableService implements PayableService {
	
	private final PayableRepository repository;
	private final PayableMapper mapper;
	
	@Override
	public PayableDTO create(PayableCreateDTO dto) {
		Payable entity = mapper.toEntity(dto);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public PayableDTO update(UUID id, PayableUpdateDTO dto) {
		Payable entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Conta a pagar não encontrada."));
		
		mapper.updateFromDto(dto, entity);
		repository.save(entity);
		return mapper.toDto(entity);
	}
	
	@Override
	public void delete(UUID id) {
		Payable entity = repository.findById(id)
			.orElseThrow(() -> new NotFoundException("Conta a pagar não encontrada."));
		
		repository.delete(entity);
	}
	
	@Override
	public PayableDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toDto)
			.orElseThrow(() -> new NotFoundException("Conta a pagar não encontrada."));
	}
	
	@Override
	public Page<PayableDTO> findAll(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toDto);
	}
}

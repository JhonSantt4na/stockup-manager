package com.stockup.StockUp.Manager.service.Others.impl;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import com.stockup.StockUp.Manager.mapper.user.AddressMapper;
import com.stockup.StockUp.Manager.model.customer.Address;
import com.stockup.StockUp.Manager.repository.user.AddressRepository;
import com.stockup.StockUp.Manager.service.Others.IAddressService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AddressService implements IAddressService {
	
	private final AddressRepository repository;
	private final AddressMapper mapper;
	
	public AddressService(AddressRepository repository, AddressMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	@Override
	public AddressResponseDTO create(AddressRequestDTO dto) {
		Address entity = mapper.toEntity(dto);
		Address saved = repository.save(entity);
		return mapper.toResponse(saved);
	}
	
	@Override
	public AddressResponseDTO update(UUID id, AddressRequestDTO dto) {
		Address address = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
		
		mapper.updateFromDto(dto, address);
		Address saved = repository.save(address);
		
		return mapper.toResponse(saved);
	}
	
	@Override
	public AddressResponseDTO findById(UUID id) {
		Address address = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
		return mapper.toResponse(address);
	}
	
	@Override
	public Page<AddressSummaryDTO> findAll(Pageable pageable) {
		return repository.findAll(pageable)
			.map(mapper::toSummary);
	}
	
	@Override
	public void softDelete(UUID id) {
		Address address = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
		
		address.setEnabled(false);
		address.setDeletedAt(LocalDateTime.now());
		repository.save(address);
	}
	
	@Override
	public void enable(UUID id) {
		Address address = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Address not found: " + id));
		
		address.setEnabled(true);
		address.setDeletedAt(null);
		repository.save(address);
	}
}
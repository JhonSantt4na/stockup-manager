package com.stockup.StockUp.Manager.service.sales.impl;


import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.mapper.sales.CustomerMapper;
import com.stockup.StockUp.Manager.model.sales.Customer;
import com.stockup.StockUp.Manager.repository.sales.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class CustomerService {
	
	private final CustomerRepository repository;
	private final CustomerMapper mapper;
	
	public CustomerService(CustomerRepository repository, CustomerMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}
	
	public CustomerResponseDTO create(CustomerRequestDTO dto) {
		Customer entity = mapper.toEntity(dto);
		Customer saved = repository.save(entity);
		return mapper.toResponse(saved);
	}
	
	public CustomerResponseDTO update(UUID id, CustomerRequestDTO dto) {
		Customer existing = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		// MapStruct with IGNORE will update non-null properties; here we can manually set or map
		if (dto.name() != null) existing.setName(dto.name());
		existing.setCpfCnpj(dto.cpfCnpj());
		existing.setPhone(dto.phone());
		existing.setEmail(dto.email());
		Customer saved = repository.save(existing);
		return mapper.toResponse(saved);
	}
	
	public CustomerResponseDTO findById(UUID id) {
		Customer customer = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		return mapper.toResponse(customer);
	}
	
	public Page<CustomerSummaryDTO> findAll(Pageable pageable) {
		return repository.findAll(pageable)
			.map(mapper::toSummary);
	}
	
	public void softDelete(UUID id) {
		Customer c = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		c.setDeletedAt(java.time.LocalDateTime.now());
		c.setEnabled(false);
		repository.save(c);
	}
	
	public void enable(UUID id) {
		Customer c = repository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
		c.setEnabled(true);
		c.setDeletedAt(null);
		repository.save(c);
	}
}

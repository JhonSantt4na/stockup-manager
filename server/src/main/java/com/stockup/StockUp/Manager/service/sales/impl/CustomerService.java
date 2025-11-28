package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.mapper.user.CustomerMapper;
import com.stockup.StockUp.Manager.model.customer.Customer;
import com.stockup.StockUp.Manager.repository.user.CustomerRepository;
import com.stockup.StockUp.Manager.service.sales.ICustomerService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional
public class CustomerService implements ICustomerService {
	
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
	
	@Override
	public Page<CustomerSummaryDTO> findAllCustom(int page, int size, String[] sort) {
		
		Pageable pageable = PageRequest.of(page, size, parseSort(sort));
		
		return repository.findAll(pageable)
			.map(mapper::toSummary);
	}
	
	private Sort parseSort(String[] sort) {
		if (sort == null || sort.length == 0) {
			return Sort.unsorted();
		}
		
		return Sort.by(
			Arrays.stream(sort)
				.map(order -> {
					String[] parts = order.split(",");
					String field = parts[0].trim();
					
					Sort.Direction direction =
						(parts.length > 1 && parts[1].trim().equalsIgnoreCase("asc"))
							? Sort.Direction.ASC
							: Sort.Direction.DESC;
					
					return new Sort.Order(direction, field);
				})
				.toList()
		);
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
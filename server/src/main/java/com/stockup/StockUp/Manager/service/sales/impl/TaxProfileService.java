package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.mapper.catalog.TaxProfileMapper;
import com.stockup.StockUp.Manager.model.catalog.TaxProfile;
import com.stockup.StockUp.Manager.repository.catalog.TaxProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxProfileService {
	
	private static final Logger logger = LoggerFactory.getLogger(TaxProfileService.class);
	private final TaxProfileRepository repository;
	private final TaxProfileMapper mapper;
	
	public TaxProfileResponseDTO create(TaxProfileRequestDTO dto) {
		logger.debug("Creating new TaxProfile with name: {}", dto.getName());
		
		if (repository.existsByName(dto.getName())) {
			logger.warn("Duplicate TaxProfile creation attempt for name: {}", dto.getName());
			throw new DuplicateResourceException("A tax profile with this name already exists.");
		}
		
		TaxProfile entity = mapper.toEntity(dto);
		TaxProfile saved = repository.save(entity);
		
		logger.info("TaxProfile created successfully: {}", saved.getId());
		return mapper.toResponse(saved);
	}
	
	public TaxProfileResponseDTO update(UUID id, TaxProfileUpdateDTO dto) {
		logger.debug("Updating TaxProfile with ID: {}", id);
		
		TaxProfile existing = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("TaxProfile not found for update: {}", id);
				return new EntityNotFoundException("Tax profile not found: " + id);
			});
		
		mapper.updateFromDto(dto, existing);
		TaxProfile saved = repository.save(existing);
		
		logger.info("TaxProfile updated successfully: {}", id);
		return mapper.toResponse(saved);
	}
	
	@Transactional(readOnly = true)
	public TaxProfileResponseDTO findById(UUID id) {
		logger.debug("Finding TaxProfile by ID: {}", id);
		
		TaxProfile entity = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("TaxProfile not found with ID: {}", id);
				return new EntityNotFoundException("Tax profile not found: " + id);
			});
		
		return mapper.toResponse(entity);
	}
	
	@Transactional(readOnly = true)
	public List<TaxProfileResponseDTO> findAll() {
		logger.debug("Listing all tax profiles");
		
		return repository.findAll()
			.stream()
			.map(mapper::toResponse)
			.toList();
	}
	
	public void delete(UUID id) {
		logger.debug("Soft deleting TaxProfile with ID: {}", id);
		
		TaxProfile existing = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("TaxProfile not found for deletion: {}", id);
				return new EntityNotFoundException("Tax profile not found: " + id);
			});
		
		existing.disable();
		repository.save(existing);
		
		logger.info("TaxProfile soft deleted successfully: {}", id);
	}
}

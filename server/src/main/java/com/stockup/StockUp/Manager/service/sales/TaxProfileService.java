package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.mapper.sales.TaxProfileMapper;
import com.stockup.StockUp.Manager.model.sales.TaxProfile;
import com.stockup.StockUp.Manager.repository.sales.TaxProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxProfileService {
	
	private final TaxProfileRepository repository;
	private final TaxProfileMapper mapper;
	
	public TaxProfileResponseDTO create(TaxProfileRequestDTO dto) {
		if (repository.existsByName(dto.getName())) {
			throw new DuplicateResourceException("A tax profile with this name already exists.");
		}
		
		TaxProfile entity = mapper.toEntity(dto);
		TaxProfile saved = repository.save(entity);
		return mapper.toResponse(saved);
	}
	
	public TaxProfileResponseDTO update(UUID id, TaxProfileUpdateDTO dto) {
		TaxProfile existing = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Tax profile not found: " + id));
		
		mapper.updateFromDto(dto, existing);
		
		TaxProfile saved = repository.save(existing);
		return mapper.toResponse(saved);
	}
	
	@Transactional(readOnly = true)
	public TaxProfileResponseDTO findById(UUID id) {
		TaxProfile entity = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Tax profile not found: " + id));
		return mapper.toResponse(entity);
	}
	
	@Transactional(readOnly = true)
	public List<TaxProfileResponseDTO> findAll() {
		return repository.findAll()
			.stream()
			.map(mapper::toResponse)
			.toList();
	}
	
	public void delete(UUID id) {
		TaxProfile existing = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Tax profile not found: " + id));
		existing.disable();
		repository.save(existing);
	}
}
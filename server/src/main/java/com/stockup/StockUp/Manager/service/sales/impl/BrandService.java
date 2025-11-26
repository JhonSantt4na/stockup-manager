package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandResponseDTO;
import com.stockup.StockUp.Manager.mapper.BrandMapper;
import com.stockup.StockUp.Manager.model.catalog.Brand;
import com.stockup.StockUp.Manager.repository.BrandRepository;
import com.stockup.StockUp.Manager.service.sales.IBrandService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandService implements IBrandService {
	
	private final BrandRepository repository;
	private final BrandMapper mapper;
	
	@Override
	public BrandResponseDTO create(BrandRequestDTO dto) {
		Brand brand = mapper.toEntity(dto);
		return mapper.toResponse(repository.save(brand));
	}
	
	@Override
	public BrandResponseDTO update(UUID id, BrandRequestDTO dto) {
		Brand brand = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
		
		mapper.updateEntityFromDTO(dto, brand);
		return mapper.toResponse(repository.save(brand));
	}
	
	@Override
	public void delete(UUID id) {
		repository.deleteById(id);
	}
	
	@Override
	public BrandResponseDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toResponse)
			.orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
	}
	
	@Override
	public Page<BrandResponseDTO> list(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toResponse);
	}
	
	@Override
	public List<BrandResponseDTO> findAll() {
		return repository.findAll().stream().map(mapper::toResponse).toList();
	}
}
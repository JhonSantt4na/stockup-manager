package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandResponseDTO;
import com.stockup.StockUp.Manager.mapper.catalog.BrandMapper;
import com.stockup.StockUp.Manager.model.catalog.Brand;
import com.stockup.StockUp.Manager.repository.catalog.BrandRepository;
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
	public BrandResponseDTO createBrand(BrandRequestDTO dto) {
		Brand brand = mapper.toEntity(dto);
		return mapper.toResponse(repository.save(brand));
	}
	
	@Override
	public BrandResponseDTO updateBrand(UUID id, BrandRequestDTO dto) {
		Brand brand = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
		
		mapper.updateEntityFromDTO(dto, brand);
		return mapper.toResponse(repository.save(brand));
	}
	
	@Override
	public void deleteBrand(UUID id) {
		repository.deleteById(id);
	}
	
	@Override
	public BrandResponseDTO findBrandById(UUID id) {
		return repository.findById(id)
			.map(mapper::toResponse)
			.orElseThrow(() -> new EntityNotFoundException("Marca não encontrada"));
	}
	
	@Override
	public Page<BrandResponseDTO> listBrand(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toResponse);
	}
	
	@Override
	public List<BrandResponseDTO> findAllBrand() {
		return repository.findAll().stream().map(mapper::toResponse).toList();
	}
}
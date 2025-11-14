package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.mapper.sales.ProductMapper;
import com.stockup.StockUp.Manager.model.sales.Product;
import com.stockup.StockUp.Manager.repository.sales.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServices {
	
	private final ProductRepository repository;
	private final ProductMapper mapper;
	
	public ProductResponseDTO create(ProductRequestDTO dto) {
		if (repository.existsBySku(dto.getSku())) {
			throw new DuplicateResourceException("A product with this SKU already exists: " + dto.getSku());
		}
		
		Product entity = mapper.toEntity(dto);
		
		Product saved = repository.save(entity);
		return mapper.toResponse(saved);
	}
	
	public ProductResponseDTO update(UUID id, ProductUpdateDTO dto) {
		Product existing = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
		
		mapper.updateFromDto(dto, existing);
		
		Product saved = repository.save(existing);
		return mapper.toResponse(saved);
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findByName(String name) {
		Product product = repository.findByNameIgnoreCase(name)
			.orElseThrow(() -> new EntityNotFoundException("Product not found with name: " + name));
		return mapper.toResponse(product);
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findBySku(String sku) {
		Product product = repository.findBySku(sku)
			.orElseThrow(() -> new EntityNotFoundException("Product not found with SKU: " + sku));
		return mapper.toResponse(product);
	}
	
	public void delete(UUID id) {
		Product product = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
		
		product.disable();
		repository.save(product);
	}
	
	@Transactional(readOnly = true)
	public Page<ProductSummaryDTO> listAll(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toSummary);
	}
	
	@Transactional(readOnly = true)
	public Page<ProductSummaryDTO> listActive(Pageable pageable) {
		return repository.findAllByEnabledTrue(pageable).map(mapper::toSummary);
	}
}

package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.Sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.mapper.catalog.ProductMapper;
import com.stockup.StockUp.Manager.model.catalog.Product;
import com.stockup.StockUp.Manager.repository.catalog.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServices {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductServices.class);
	
	private final ProductRepository repository;
	private final ProductMapper mapper;
	
	public ProductResponseDTO create(ProductRequestDTO dto) {
		logger.debug("Creating new product with SKU: {}", dto.getSku());
		
		if (repository.existsBySku(dto.getSku())) {
			logger.warn("Attempt to create product with duplicate SKU: {}", dto.getSku());
			throw new DuplicateResourceException("A product with this SKU already exists: " + dto.getSku());
		}
		
		Product entity = mapper.toEntity(dto);
		Product saved = repository.save(entity);
		
		logger.info("Product created successfully: {}", saved.getId());
		return mapper.toResponse(saved);
	}
	
	public ProductResponseDTO update(UUID id, ProductUpdateDTO dto) {
		logger.debug("Updating product with ID: {}", id);
		
		Product existing = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Product not found for update: {}", id);
				return new EntityNotFoundException("Product not found: " + id);
			});
		
		mapper.updateFromDto(dto, existing);
		
		Product saved = repository.save(existing);
		
		logger.info("Product updated successfully: {}", id);
		return mapper.toResponse(saved);
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findByName(String name) {
		logger.debug("Searching product by name: {}", name);
		
		Product product = repository.findByNameIgnoreCase(name)
			.orElseThrow(() -> {
				logger.warn("Product not found with name: {}", name);
				return new EntityNotFoundException("Product not found with name: " + name);
			});
		
		return mapper.toResponse(product);
	}
	
	@Transactional(readOnly = true)
	public ProductResponseDTO findBySku(String sku) {
		logger.debug("Searching product by SKU: {}", sku);
		
		Product product = repository.findBySku(sku)
			.orElseThrow(() -> {
				logger.warn("Product not found with SKU: {}", sku);
				return new EntityNotFoundException("Product not found with SKU: " + sku);
			});
		
		return mapper.toResponse(product);
	}
	
	public void delete(UUID id) {
		logger.debug("Soft deleting product with ID: {}", id);
		
		Product product = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Product not found for deletion: {}", id);
				return new EntityNotFoundException("Product not found: " + id);
			});
		
		product.disable();
		repository.save(product);
		
		logger.info("Product soft deleted successfully: {}", id);
	}
	
	@Transactional(readOnly = true)
	public Page<ProductSummaryDTO> listAll(Pageable pageable) {
		logger.debug("Listing all products with pagination");
		return repository.findAll(pageable).map(mapper::toSummary);
	}
	
	@Transactional(readOnly = true)
	public Page<ProductSummaryDTO> listActive(Pageable pageable) {
		logger.debug("Listing all ACTIVE products with pagination");
		return repository.findAllByEnabledTrue(pageable).map(mapper::toSummary);
	}
}

package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.mapper.catalog.CategoryMapper;
import com.stockup.StockUp.Manager.model.catalog.Category;
import com.stockup.StockUp.Manager.repository.catalog.CategoryRepository;
import com.stockup.StockUp.Manager.service.sales.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServices implements ICategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryServices.class);
	
	private final CategoryRepository repository;
	private final CategoryMapper mapper;
	
	
	@Override
	public CategoryResponseDTO create(CategoryRequestDTO dto) {
		logger.info("Request to create category with name [{}]", dto.name());
		
		if (repository.existsByNameIgnoreCase(dto.name())) {
			logger.warn("Duplicate category creation attempt with name [{}]", dto.name());
			throw new DuplicateResourceException("Category already exists: " + dto.name());
		}
		
		Category entity = mapper.toEntity(dto);
		Category saved = repository.save(entity);
		
		logger.debug("Category [{}] created successfully with ID [{}]", saved.getName(), saved.getId());
		return mapper.toResponse(saved);
	}
	
	
	@Override
	public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {
		logger.info("Request to update category [{}]", id);
		
		Category existing = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Category update failed — ID [{}] not found", id);
				return new NotFoundException("Category not found: " + id);
			});
		
		repository.findByNameIgnoreCase(dto.name())
			.filter(cat -> !cat.getId().equals(id))
			.ifPresent(cat -> {
				logger.warn("Duplicate category name [{}] on update for ID [{}]", dto.name(), id);
				throw new DuplicateResourceException("Category name already exists: " + dto.name());
			});
		
		existing.setName(dto.name());
		existing.setDescription(dto.description());
		
		Category saved = repository.save(existing);
		
		logger.debug("Category [{}] updated successfully", saved.getId());
		return mapper.toResponse(saved);
	}
	
	
	@Override
	public void delete(UUID id) {
		logger.info("Request to soft-delete category [{}]", id);
		
		Category category = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Category delete failed — ID [{}] not found", id);
				return new EntityNotFoundException("Category not found: " + id);
			});
		
		category.setEnabled(false);
		category.setDeletedAt(LocalDateTime.now());
		
		repository.save(category);
		
		logger.debug("Category [{}] soft-deleted successfully", id);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<CategoryResponseDTO> getAllActive(Pageable pageable) {
		logger.info("Request to fetch all active categories");
		return repository.findAllByEnabledTrue(pageable)
			.map(mapper::toResponse);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<CategoryResponseDTO> getAll(Pageable pageable) {
		logger.info("Request to fetch all categories (active + inactive)");
		return repository.findAll(pageable)
			.map(mapper::toResponse);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public CategoryResponseDTO findByName(String name) {
		logger.info("Request to fetch category by name [{}]", name);
		
		Category category = repository.findByNameIgnoreCase(name)
			.orElseThrow(() -> {
				logger.warn("Category [{}] not found", name);
				return new NotFoundException("Category not found with name: " + name);
			});
		
		logger.debug("Category [{}] found with ID [{}]", name, category.getId());
		return mapper.toResponse(category);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public CategoryResponseDTO findById(UUID id) {
		logger.info("Request to fetch category by ID [{}]", id);
		
		Category category = repository.findById(id)
			.orElseThrow(() -> {
				logger.warn("Category with ID [{}] not found", id);
				return new NotFoundException("Category not found: " + id);
			});
		
		logger.debug("Category with ID [{}] retrieved successfully", id);
		return mapper.toResponse(category);
	}
}

package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.exception.NotFoundException;
import com.stockup.StockUp.Manager.mapper.sales.CategoryMapper;
import com.stockup.StockUp.Manager.model.sales.Category;
import com.stockup.StockUp.Manager.repository.sales.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServices {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryServices.class);
	private final CategoryRepository categoryRepository;
	private final CategoryMapper categoryMapper;
	
	public Category createCategory(CategoryRequestDTO dto) {
		logger.info("Creating new category [{}]", dto.name());
		
		if (categoryRepository.existsByName(dto.name())) {
			logger.warn("Attempt to create duplicate category [{}]", dto.name());
			throw new DuplicateResourceException("Category already exists: " + dto.name());
		}
		
		Category category = categoryMapper.toEntity(dto);
		Category saved = categoryRepository.save(category);
		
		logger.info("Category successfully created [{} - ID: {}]", saved.getName(), saved.getId());
		return saved;
	}
	
	public Category updateCategory(UUID id, CategoryRequestDTO dto) {
		logger.info("Updating category [{}] to name [{}] and description [{}]", id, dto.name(), dto.description());
		
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
		
		if (!category.getName().equalsIgnoreCase(dto.name()) &&
			categoryRepository.findByName(dto.name()).isPresent()) {
			logger.warn("Attempt to rename category [{}] to an existing name [{}]", category.getName(), dto.name());
			throw new DuplicateResourceException("Category name already exists: " + dto.name());
		}
		
		category.setName(dto.name());
		category.setDescription(dto.description());
		
		Category updated = categoryRepository.save(category);
		
		logger.info("Category [{}] successfully updated to name [{}]", id, updated.getName());
		return updated;
	}
	
	public void deleteCategory(UUID id) {
		logger.info("Request to delete category [{}]", id);
		
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
		
		if (category.isDeleted()) {
			logger.warn("Attempt to delete an already deleted category [{}]", id);
			throw new IllegalStateException("Category already deleted: " + id);
		}
		
		category.setDeletedAt(LocalDateTime.now());
		category.setEnabled(false);
		
		categoryRepository.save(category);
		
		logger.info("Category [{}] successfully soft deleted", id);
	}
	
	public Page<CategoryResponseDTO> getAllActiveCategories(Pageable pageable) {
		log.info("Fetching active categories with pagination: page={}, size={}",
			pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Category> categories = categoryRepository.findAllActive(pageable);
		
		
		if (categories.isEmpty()) {
			log.warn("No active categories found");
		} else {
			log.info("Found {} active categories", categories.getTotalElements());
		}
		
		return categories.map(categoryMapper::toResponse);
	}
	
	public Page<CategoryResponseDTO> getAllCategories(Pageable pageable) {
		logger.debug("Listing all categories with pagination: page={}, size={}",
			pageable.getPageNumber(), pageable.getPageSize());
		
		Page<Category> categoryPage = categoryRepository.findAll(pageable);
		
		logger.info("Total categories found [{}]", categoryPage.getTotalElements());
		
		return categoryPage.map(categoryMapper::toResponse);
	}
	
	public Category getCategoryByName(String name) {
		logger.debug("Fetching category by name [{}]", name);
		
		return categoryRepository.findByName(name)
			.orElseThrow(() -> new NotFoundException("Category not found with name: " + name));
	}
	
}
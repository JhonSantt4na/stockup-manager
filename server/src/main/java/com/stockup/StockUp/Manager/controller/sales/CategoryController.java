package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.controller.sales.docs.CategoryControllerDocs;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.model.sales.Category;
import com.stockup.StockUp.Manager.service.sales.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {
	
	private final CategoryServices categoryServices;
	
	@Override
	@PostMapping
	public ResponseEntity<Category> createCategory(@RequestBody CategoryRequestDTO dto) {
		Category created = categoryServices.createCategory(dto);
		return ResponseEntity.ok(created);
	}
	
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<Category> updateCategory(
		@PathVariable UUID id,
		@RequestBody CategoryRequestDTO dto) {
		
		Category updated = categoryServices.updateCategory(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@GetMapping("/by-name/{name}")
	public ResponseEntity<Category> getPermissionByCategoryName(@PathVariable String name) {
		Category category = categoryServices.getCategoryByName(name);
		return ResponseEntity.ok(category);
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
		categoryServices.deleteCategory(id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping
	public ResponseEntity<Page<CategoryResponseDTO>> listCategory(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> categories = categoryServices.getAllCategories(pageable);
		return ResponseEntity.ok(categories);
	}
	
	@Override
	@GetMapping("/active")
	public ResponseEntity<Page<CategoryResponseDTO>> getAllPermissionsIsActive(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> activeCategories = categoryServices.getAllActiveCategories(pageable);
		return ResponseEntity.ok(activeCategories);
	}
	
	private Pageable buildPageable(int page, int size, String[] sort) {
		if (sort.length == 0) {
			return PageRequest.of(page, size);
		}
		
		String sortField = sort[0];
		Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc")
			? Sort.Direction.ASC
			: Sort.Direction.DESC;
		
		return PageRequest.of(page, size, Sort.by(direction, sortField));
	}
}

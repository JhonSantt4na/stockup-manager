package com.stockup.StockUp.Manager.controller.catalog;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.catalog.docs.CategoryControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {
	
	private final ICategoryService categoryService;
	
	@Override
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryResponseDTO> createCategory(
		@Valid @RequestBody CategoryRequestDTO dto) {
		try {
			CategoryResponseDTO created = categoryService.create(dto);
			AuditLogger.log("CATEGORY_CREATE", getCurrentUser(), "SUCCESS",
				"Category creating: " + dto.name());
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
			
		} catch (DuplicateResourceException e) {
			AuditLogger.log("CATEGORY_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
			
		} catch (Exception e) {
			AuditLogger.log("CATEGORY_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			throw new RuntimeException("Error creating category", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<CategoryResponseDTO> updateCategory(
		@PathVariable UUID id,
		@Valid @RequestBody CategoryRequestDTO dto) {
		
		CategoryResponseDTO updated = categoryService.update(id, dto);
		AuditLogger.log("CATEGORY_UPDATE", getCurrentUser(), "SUCCESS",
			"Category Updated: " + id);
		
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@GetMapping("/by-name/{name}")
	public ResponseEntity<CategoryResponseDTO> getCategoryByName(@PathVariable String name) {
		CategoryResponseDTO response = categoryService.findByName(name);
		return ResponseEntity.ok(response);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
		categoryService.delete(id);
		AuditLogger.log("CATEGORY_DELETE", getCurrentUser(), "SUCCESS",
			"Category Disabled: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping()
	public ResponseEntity<Page<CategoryResponseDTO>> listCategories(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> response = categoryService.getAll(pageable);
		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping("/active")
	public ResponseEntity<Page<CategoryResponseDTO>> listActiveCategories(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> response = categoryService.getAllActive(pageable);
		return ResponseEntity.ok(response);
	}
	
	private Pageable buildPageable(int page, int size, String[] sort) {
		String field = "createdAt";
		Sort.Direction direction = Sort.Direction.DESC;
		
		if (sort.length > 0 && !sort[0].isBlank()) {
			field = sort[0];
		}
		if (sort.length > 1 && sort[1].equalsIgnoreCase("asc")) {
			direction = Sort.Direction.ASC;
		}
		
		return PageRequest.of(page, size, Sort.by(direction, field));
	}
}
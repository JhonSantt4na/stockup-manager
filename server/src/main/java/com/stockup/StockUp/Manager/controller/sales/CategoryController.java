package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.controller.sales.docs.CategoryControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.service.sales.ICategoryService;
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
	
	private final ICategoryService categoryService;
	
	@Override
	public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO dto) {
		CategoryResponseDTO created = categoryService.create(dto);
		return ResponseEntity.status(201).body(created);
	}
	
	@Override
	public ResponseEntity<CategoryResponseDTO> updateCategory(UUID id, CategoryRequestDTO dto) {
		CategoryResponseDTO updated = categoryService.update(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	public ResponseEntity<CategoryResponseDTO> getCategoryByName(String name) {
		CategoryResponseDTO response = categoryService.findByName(name);
		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Void> deleteCategory(UUID id) {
		categoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	public ResponseEntity<Page<CategoryResponseDTO>> listCategories(int page, int size, String[] sort) {
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> categories = categoryService.getAll(pageable);
		return ResponseEntity.ok(categories);
	}
	
	@Override
	public ResponseEntity<Page<CategoryResponseDTO>> listActiveCategories(int page, int size, String[] sort) {
		Pageable pageable = buildPageable(page, size, sort);
		Page<CategoryResponseDTO> categories = categoryService.getAllActive(pageable);
		return ResponseEntity.ok(categories);
	}
	
	private Pageable buildPageable(int page, int size, String[] sort) {
		if (sort.length == 0) {
			return PageRequest.of(page, size);
		}
		
		String field = sort[0];
		Sort.Direction direction =
			sort.length > 1 && sort[1].equalsIgnoreCase("asc")
				? Sort.Direction.ASC
				: Sort.Direction.DESC;
		
		return PageRequest.of(page, size, Sort.by(direction, field));
	}
}
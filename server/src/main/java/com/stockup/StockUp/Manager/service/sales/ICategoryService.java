package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ICategoryService {
	CategoryResponseDTO createCategory(CategoryRequestDTO dto);
	CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO dto);
	void deleteCategory(UUID id);
	Page<CategoryResponseDTO> getAllCategoryActive(Pageable pageable);
	Page<CategoryResponseDTO> getAllCategory(Pageable pageable);
	CategoryResponseDTO findCategoryByName(String name);
	CategoryResponseDTO findCategoryById(UUID id);
}
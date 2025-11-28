package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ICategoryService {
	
	CategoryResponseDTO create(CategoryRequestDTO dto);
	
	CategoryResponseDTO update(UUID id, CategoryRequestDTO dto);
	
	void delete(UUID id);
	
	Page<CategoryResponseDTO> getAllActive(Pageable pageable);
	
	Page<CategoryResponseDTO> getAll(Pageable pageable);
	
	CategoryResponseDTO findByName(String name);
	
	CategoryResponseDTO findById(UUID id);
}
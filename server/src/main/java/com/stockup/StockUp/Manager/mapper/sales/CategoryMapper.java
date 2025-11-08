package com.stockup.StockUp.Manager.mapper.sales;

import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.model.sales.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
	
	Category toEntity(CategoryRequestDTO dto);
	CategoryResponseDTO toResponse(Category entity);
	CategoryResponseDTO toSummary(Category entity);
}
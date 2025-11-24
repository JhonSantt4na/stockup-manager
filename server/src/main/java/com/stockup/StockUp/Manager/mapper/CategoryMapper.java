package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.model.catalog.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
	
	Category toEntity(CategoryRequestDTO dto);
	CategoryResponseDTO toResponse(Category entity);
	CategoryResponseDTO toSummary(Category entity);
}
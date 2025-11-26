package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.model.catalog.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Category toEntity(CategoryRequestDTO dto);
	CategoryResponseDTO toResponse(Category entity);
	CategoryResponseDTO toSummary(Category entity);
}
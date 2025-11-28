package com.stockup.StockUp.Manager.mapper.catalog;

import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandResponseDTO;
import com.stockup.StockUp.Manager.model.catalog.Brand;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BrandMapper {
	
	Brand toEntity(BrandRequestDTO dto);
	
	BrandResponseDTO toResponse(Brand entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntityFromDTO(BrandRequestDTO dto, @MappingTarget Brand entity);
}

package com.stockup.StockUp.Manager.mapper.catalog;

import com.stockup.StockUp.Manager.dto.Sales.UniteOfMeasure.UnitOfMeasureRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.UniteOfMeasure.UnitOfMeasureResponseDTO;
import com.stockup.StockUp.Manager.model.catalog.UnitOfMeasure;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UnitOfMeasureMapper {
	
	UnitOfMeasure toEntity(UnitOfMeasureRequestDTO dto);
	
	UnitOfMeasureResponseDTO toResponse(UnitOfMeasure entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntityFromDTO(UnitOfMeasureRequestDTO dto, @MappingTarget UnitOfMeasure entity);
}
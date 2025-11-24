package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.Enums.UnitOfMeasure;
import com.stockup.StockUp.Manager.dto.sales.UniteOfMeasure.UnitOfMeasureRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.UniteOfMeasure.UnitOfMeasureResponseDTO;
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
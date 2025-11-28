package com.stockup.StockUp.Manager.mapper.stock;

import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;
import com.stockup.StockUp.Manager.model.stock.Warehouse;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WarehouseMapper {
	
	Warehouse toEntity(WarehouseRequestDTO dto);
	
	WarehouseResponseDTO toDTO(Warehouse entity);
	
	void updateEntityFromDTO(WarehouseRequestDTO dto, @MappingTarget Warehouse entity);
}
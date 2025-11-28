package com.stockup.StockUp.Manager.mapper.stock;


import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import com.stockup.StockUp.Manager.model.stock.Stock;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockMapper {
	
	@Mapping(target = "product.id", source = "productId")
	@Mapping(target = "location.id", source = "locationId")
	Stock toEntity(StockRequestDTO dto);
	
	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "locationId", source = "location.id")
	StockResponseDTO toDTO(Stock entity);
	
	@InheritConfiguration(name = "toEntity")
	void updateEntityFromDTO(StockRequestDTO dto, @MappingTarget Stock entity);
}
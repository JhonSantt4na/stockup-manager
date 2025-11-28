package com.stockup.StockUp.Manager.mapper.stock;

import com.stockup.StockUp.Manager.Enums.Stock.MovementReason;
import com.stockup.StockUp.Manager.Enums.Stock.MovementType;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;
import com.stockup.StockUp.Manager.model.stock.StockMovement;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockMovementMapper {
	
	@Mapping(target = "product.id", source = "productId")
	@Mapping(target = "movementType", source = "movementType", qualifiedByName = "toMovementType")
	@Mapping(target = "reason", source = "reason", qualifiedByName = "toMovementReason")
	StockMovement toEntity(StockMovementRequestDTO dto);
	
	@Mapping(target = "productId", source = "product.id")
	@Mapping(target = "movementType", source = "movementType", qualifiedByName = "movementTypeToString")
	@Mapping(target = "reason", source = "reason", qualifiedByName = "movementReasonToString")
	StockMovementResponseDTO toDTO(StockMovement entity);
	
	@InheritConfiguration(name = "toEntity")
	void updateEntityFromDTO(StockMovementRequestDTO dto, @MappingTarget StockMovement entity);
	
	
	@Named("toMovementType")
	default MovementType toMovementType(String value) {
		return MovementType.valueOf(value.toUpperCase());
	}
	
	@Named("movementTypeToString")
	default String movementTypeToString(MovementType type) {
		return type.name();
	}
	
	@Named("toMovementReason")
	default MovementReason toMovementReason(String value) {
		return MovementReason.valueOf(value.toUpperCase());
	}
	
	@Named("movementReasonToString")
	default String movementReasonToString(MovementReason reason) {
		return reason.name();
	}
}

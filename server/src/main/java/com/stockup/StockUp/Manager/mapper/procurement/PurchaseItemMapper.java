package com.stockup.StockUp.Manager.mapper.procurement;

import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemResponseDTO;
import com.stockup.StockUp.Manager.mapper.catalog.ProductMapper;
import com.stockup.StockUp.Manager.model.procurement.PurchaseItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = { ProductMapper.class })
public interface PurchaseItemMapper {
	
	PurchaseItem toEntity(PurchaseItemRequestDTO dto);
	
	PurchaseItemResponseDTO toResponse(PurchaseItem entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget PurchaseItem entity, PurchaseItemRequestDTO dto);
}

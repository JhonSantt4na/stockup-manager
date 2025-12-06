package com.stockup.StockUp.Manager.mapper.procurement;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderResponseDTO;
import com.stockup.StockUp.Manager.model.procurement.PurchaseOrder;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = { SupplierMapper.class, PurchaseItemMapper.class })
public interface PurchaseOrderMapper {
	
	PurchaseOrder toEntity(PurchaseOrderRequestDTO dto);
	
	PurchaseOrderResponseDTO toResponse(PurchaseOrder entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget PurchaseOrder entity, PurchaseOrderRequestDTO dto);
}
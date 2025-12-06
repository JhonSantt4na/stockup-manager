package com.stockup.StockUp.Manager.mapper.procurement;

import com.stockup.StockUp.Manager.dto.procurement.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.Supplier.SupplierResponseDTO;
import com.stockup.StockUp.Manager.model.procurement.Supplier;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupplierMapper {
	
	Supplier toEntity(SupplierRequestDTO dto);
	
	SupplierResponseDTO toResponse(Supplier entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void update(@MappingTarget Supplier entity, SupplierRequestDTO dto);
}
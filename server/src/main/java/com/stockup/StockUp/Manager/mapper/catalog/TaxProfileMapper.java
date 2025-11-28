package com.stockup.StockUp.Manager.mapper.catalog;

import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileUpdateDTO;
import com.stockup.StockUp.Manager.model.catalog.TaxProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaxProfileMapper {
	
	TaxProfile toEntity(TaxProfileRequestDTO dto);
	
	TaxProfileResponseDTO toResponse(TaxProfile entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromDto(TaxProfileUpdateDTO dto, @MappingTarget TaxProfile entity);
}
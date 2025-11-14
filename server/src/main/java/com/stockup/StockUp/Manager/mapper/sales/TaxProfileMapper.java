package com.stockup.StockUp.Manager.mapper.sales;

import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileUpdateDTO;
import com.stockup.StockUp.Manager.model.sales.TaxProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaxProfileMapper {
	
	TaxProfile toEntity(TaxProfileRequestDTO dto);
	
	TaxProfileResponseDTO toResponse(TaxProfile entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateFromDto(TaxProfileUpdateDTO dto, @MappingTarget TaxProfile entity);
}
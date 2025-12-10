package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PayableMapper {
	
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "payment", ignore = true)
	@Mapping(target = "installmentTotal", ignore = true)
	@Mapping(target = "installmentNumber", ignore = true)
	@Mapping(target = "status", ignore = true)
	Payable toEntity(PayableRequestDTO dto);
	
	PayableResponseDTO toResponse(Payable entity);
	
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "payment", ignore = true)
	@Mapping(target = "installmentTotal", ignore = true)
	@Mapping(target = "installmentNumber", ignore = true)
	@Mapping(target = "status", ignore = true)
	void updateEntityFromDTO(PayableRequestDTO dto, @MappingTarget Payable entity);
}
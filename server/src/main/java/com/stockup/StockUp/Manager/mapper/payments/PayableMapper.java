package com.stockup.StockUp.Manager.mapper.payments;

import com.stockup.StockUp.Manager.dto.payments.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.payable.PayableResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PayableMapper {
	
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "payment", ignore = true)
	@Mapping(target = "installmentTotal", ignore = true)
	@Mapping(target = "installmentNumber", ignore = true)
	@Mapping(target = "status", ignore = true)
	Payable toEntity(PayableRequestDTO dto);
	
	@Mapping(target = "paymentDate", ignore = true)
	@Mapping(target = "issueDate", ignore = true)
	@Mapping(target = "dueDate", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "category", ignore = true)
	@Mapping(target = "supplierId", ignore = true)
	PayableResponseDTO toResponse(Payable entity);
	
	
	@Mapping(target = "provider", ignore = true)
	@Mapping(target = "payment", ignore = true)
	@Mapping(target = "installmentTotal", ignore = true)
	@Mapping(target = "installmentNumber", ignore = true)
	@Mapping(target = "status", ignore = true)
	void updateEntityFromDTO(PayableRequestDTO dto, @MappingTarget Payable entity);
}

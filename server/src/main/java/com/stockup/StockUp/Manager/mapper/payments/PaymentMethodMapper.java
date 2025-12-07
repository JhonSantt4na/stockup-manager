package com.stockup.StockUp.Manager.mapper.payments;

import com.stockup.StockUp.Manager.dto.payments.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.payments.payment.PaymentMethodResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentMethod;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMethodMapper {
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	PaymentMethod toEntity(PaymentMethodRequestDTO dto);
	
	PaymentMethodResponseDTO toResponse(PaymentMethod entity);
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	void updateEntityFromDTO(PaymentMethodRequestDTO dto, @MappingTarget PaymentMethod entity);
}
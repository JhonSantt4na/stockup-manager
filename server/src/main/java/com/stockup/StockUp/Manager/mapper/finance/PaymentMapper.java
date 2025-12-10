package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payments.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {PaymentMethodMapper.class})
public interface PaymentMapper {
	
	@Mapping(target = "order", ignore = true)
	@Mapping(target = "code", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "paidAt", ignore = true)
	@Mapping(target = "method", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "gatewayTransaction", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	Payment toEntity(PaymentRequestDTO dto);
	
	@Mapping(target = "gateway", ignore = true)
	PaymentResponseDTO toResponse(Payment entity);
	
	@Mapping(target = "order", ignore = true)
	@Mapping(target = "code", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "paidAt", ignore = true)
	@Mapping(target = "method", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "gatewayTransaction", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	void updateEntityFromDTO(PaymentRequestDTO dto, @MappingTarget Payment entity);
}

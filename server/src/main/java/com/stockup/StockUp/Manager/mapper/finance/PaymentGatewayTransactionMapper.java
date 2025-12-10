package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentGatewayTransactionResponseDTO;
import com.stockup.StockUp.Manager.model.finance.payments.PaymentGatewayTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
	componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentGatewayTransactionMapper {
	
	PaymentGatewayTransactionResponseDTO toResponse(PaymentGatewayTransaction entity);
	
}
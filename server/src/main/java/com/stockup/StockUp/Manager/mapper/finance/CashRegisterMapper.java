package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.payments.cash.CashRegisterResponseDTO;
import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {CashEntryMapper.class})
public interface CashRegisterMapper {
	
	CashRegisterResponseDTO toResponse(CashRegister entity);
}

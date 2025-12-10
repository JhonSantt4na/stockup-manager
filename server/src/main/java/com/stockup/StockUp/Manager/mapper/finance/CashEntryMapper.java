package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CashEntryMapper {
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "cashRegister", ignore = true)
	CashEntry toEntity(CashEntryRequestDTO dto);
	
	CashEntryResponseDTO toResponse(CashEntry entity);
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "cashRegister", ignore = true)
	void updateEntityFromDTO(CashEntryRequestDTO dto, @MappingTarget CashEntry entity);
}
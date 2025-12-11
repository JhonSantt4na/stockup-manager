package com.stockup.StockUp.Manager.mapper.finance;

import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;
import com.stockup.StockUp.Manager.model.finance.cash.CashMovement;
import org.mapstruct.*;

import java.util.Scanner;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CashMovementMapper {
	
	@Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
	CashMovement toEntity(CashMovementRequestDTO dto);
	
	@Mapping(target = "sessionId", source = "session.id")
	CashMovementResponseDTO toDTO(CashMovement entity);
}
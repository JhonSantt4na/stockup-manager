package com.stockup.StockUp.Manager.service.stock;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IStockMovementService {
	StockMovementResponseDTO registerMovement(StockMovementRequestDTO dto);
	
	StockMovementResponseDTO getById(UUID id);
	
	List<StockMovementResponseDTO> listAll();
	
	void delete(UUID id);
}

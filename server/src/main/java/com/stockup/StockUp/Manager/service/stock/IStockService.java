package com.stockup.StockUp.Manager.service.stock;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IStockService {
	StockResponseDTO createStock(StockRequestDTO dto);
	StockResponseDTO updateStock(UUID id, StockRequestDTO dto);
	StockResponseDTO getStockById(UUID id);
	List<StockResponseDTO> listAllStock();
	void deleteStock(UUID id);
}
package com.stockup.StockUp.Manager.service.stock;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IStockService {
	
	StockResponseDTO create(StockRequestDTO dto);
	
	StockResponseDTO update(UUID id, StockRequestDTO dto);
	
	StockResponseDTO getById(UUID id);
	
	List<StockResponseDTO> listAll();
	
	void delete(UUID id);
}
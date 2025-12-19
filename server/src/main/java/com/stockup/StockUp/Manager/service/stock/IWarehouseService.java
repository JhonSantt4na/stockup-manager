package com.stockup.StockUp.Manager.service.stock;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IWarehouseService {
	
	WarehouseResponseDTO createWarehouse(WarehouseRequestDTO dto);
	WarehouseResponseDTO updateWarehouse(UUID id, WarehouseRequestDTO dto);
	WarehouseResponseDTO getWarehouseById(UUID id);
	Page<WarehouseResponseDTO> listAllWarehouse(Integer page, Integer size, WarehouseType type);
	void deleteWarehouse(UUID id);
}
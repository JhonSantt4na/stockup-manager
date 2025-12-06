package com.stockup.StockUp.Manager.service.stock;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;

import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IWarehouseService {
	
	WarehouseResponseDTO create(WarehouseRequestDTO dto);
	
	WarehouseResponseDTO update(UUID id, WarehouseRequestDTO dto);
	
	WarehouseResponseDTO getById(UUID id);
	
	Page<WarehouseResponseDTO> listAll(Integer page, Integer size, WarehouseType type);
	
	void delete(UUID id);
}
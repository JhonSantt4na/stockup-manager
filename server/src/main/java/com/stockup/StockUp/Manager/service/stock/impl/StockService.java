package com.stockup.StockUp.Manager.service.stock.impl;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import com.stockup.StockUp.Manager.mapper.stock.StockMapper;
import com.stockup.StockUp.Manager.model.stock.Stock;
import com.stockup.StockUp.Manager.model.stock.Warehouse;
import com.stockup.StockUp.Manager.repository.stock.StockRepository;
import com.stockup.StockUp.Manager.repository.stock.WarehouseRepository;
import com.stockup.StockUp.Manager.service.stock.IStockService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService implements IStockService {
	
	private final StockRepository repository;
	private final WarehouseRepository warehouseRepository;
	private final StockMapper mapper;
	
	@Override
	public StockResponseDTO create(StockRequestDTO dto) {
		log.info("Creating new stock item: {}", dto);
		
		UUID warehouseId = UUID.fromString(dto.warehouseId());
		Warehouse warehouse = warehouseRepository.findById(warehouseId)
			.orElseThrow(() -> {
				log.warn("Warehouse not found for ID {}", warehouseId);
				return new EntityNotFoundException("Warehouse not found");
			});
		
		Stock stock = mapper.toEntity(dto);
		stock.setWarehouse(warehouse);
		stock.setQuantity(BigDecimal.ZERO);
		
		repository.save(stock);
		
		log.info("Stock created successfully. ID: {}", stock.getId());
		return mapper.toDTO(stock);
	}
	
	@Override
	public StockResponseDTO update(UUID id, StockRequestDTO dto) {
		log.info("Updating stock {} with data {}", id, dto);
		
		Stock stock = repository.findById(id)
			.orElseThrow(() -> {
				log.warn("Stock not found for ID {}", id);
				return new EntityNotFoundException("Stock not found");
			});
		
		mapper.updateEntityFromDTO(dto, stock);
		
		UUID warehouseId = UUID.fromString(dto.warehouseId());
		if (!stock.getWarehouse().getId().equals(warehouseId)) {
			Warehouse warehouse = warehouseRepository.findById(warehouseId)
				.orElseThrow(() -> {
					log.warn("Warehouse not found for ID {}", warehouseId);
					return new EntityNotFoundException("Warehouse not found");
				});
			
			stock.setWarehouse(warehouse);
		}
		
		repository.save(stock);
		
		log.info("Stock updated successfully. ID: {}", id);
		return mapper.toDTO(stock);
	}
	
	@Override
	public StockResponseDTO getById(UUID id) {
		Stock stock = repository.findById(id)
			.orElseThrow(() -> {
				log.warn("Stock not found for ID {}", id);
				return new EntityNotFoundException("Stock not found");
			});
		
		return mapper.toDTO(stock);
	}
	
	@Override
	public List<StockResponseDTO> listAll() {
		log.info("Listing all stock records");
		return repository.findAll().stream().map(mapper::toDTO).toList();
	}
	
	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id)) {
			log.warn("Attempted to delete stock, but it does not exist: {}", id);
			throw new EntityNotFoundException("Stock not found");
		}
		
		repository.deleteById(id);
		log.info("Stock deleted successfully. ID: {}", id);
	}
}
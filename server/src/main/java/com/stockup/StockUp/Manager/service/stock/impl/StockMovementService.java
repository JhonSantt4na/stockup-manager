package com.stockup.StockUp.Manager.service.stock.impl;

import com.stockup.StockUp.Manager.Enums.Stock.MovementType;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;
import com.stockup.StockUp.Manager.exception.BusinessException;
import com.stockup.StockUp.Manager.mapper.stock.StockMovementMapper;
import com.stockup.StockUp.Manager.model.catalog.Product;
import com.stockup.StockUp.Manager.model.stock.Stock;
import com.stockup.StockUp.Manager.model.stock.StockMovement;
import com.stockup.StockUp.Manager.model.stock.Warehouse;
import com.stockup.StockUp.Manager.repository.catalog.ProductRepository;
import com.stockup.StockUp.Manager.repository.stock.StockMovementRepository;
import com.stockup.StockUp.Manager.repository.stock.StockRepository;
import com.stockup.StockUp.Manager.repository.stock.WarehouseRepository;
import com.stockup.StockUp.Manager.service.stock.IStockMovementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMovementService implements IStockMovementService {
	
	private final StockMovementRepository repository;
	private final StockRepository stockRepository;
	private final ProductRepository productRepository;
	private final WarehouseRepository warehouseRepository;
	private final StockMovementMapper mapper;
	
	@Override
	public StockMovementResponseDTO registerMovement(StockMovementRequestDTO dto) {
		log.info("Registering stock movement: {}", dto);
		
		Product product = productRepository.findById(UUID.fromString(dto.productId()))
			.orElseThrow(() -> new EntityNotFoundException("Product not found"));
		
		Warehouse warehouse = warehouseRepository.findById(UUID.fromString(dto.warehouseId()))
			.orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));
		
		Stock stock = stockRepository.findByProductIdAndWarehouseId(
				UUID.fromString(dto.productId()),
				UUID.fromString(dto.warehouseId()))
			.orElseThrow(() -> new EntityNotFoundException("Stock record not found"));
		
		BigDecimal previousQty = stock.getQuantity();
		BigDecimal movementQty = dto.quantity();
		BigDecimal finalQty;
		
		if (dto.movementType() == MovementType.IN) {
			finalQty = previousQty.add(movementQty);
		} else {
			if (previousQty.compareTo(movementQty) < 0) {
				throw new BusinessException("Insufficient stock to process this movement");
			}
			finalQty = previousQty.subtract(movementQty);
		}
		
		log.debug("Stock update | Product={}, Warehouse={}, Previous={}, Movement={}, Final={}",
			product.getId(), warehouse.getId(), previousQty, movementQty, finalQty);
		
		stock.setQuantity(finalQty);
		stock.setUpdatedAt(LocalDateTime.now());
		stockRepository.save(stock);
		
		StockMovement movement = mapper.toEntity(dto);
		movement.setProduct(product);
		movement.setWarehouse(warehouse);
		movement.setPreviousQuantity(previousQty);
		movement.setFinalQuantity(finalQty);
		movement.setTimestamp(LocalDateTime.now());
		
		repository.save(movement);
		
		log.info("Stock movement registered successfully. MovementID={}", movement.getId());
		return mapper.toDTO(movement);
	}
	
	@Override
	public StockMovementResponseDTO getById(UUID id) {
		StockMovement movement = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Movement not found"));
		return mapper.toDTO(movement);
	}
	
	@Override
	public List<StockMovementResponseDTO> listAll() {
		return repository.findAll()
			.stream()
			.map(mapper::toDTO)
			.toList();
	}
	
	@Override
	public void delete(UUID id) {
		if (!repository.existsById(id)) {
			throw new EntityNotFoundException("Movement not found");
		}
		repository.deleteById(id);
		log.info("Stock movement deleted. id={}", id);
	}
}
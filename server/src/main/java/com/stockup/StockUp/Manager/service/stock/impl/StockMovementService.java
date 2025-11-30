package com.stockup.StockUp.Manager.service.stock.impl;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;
import com.stockup.StockUp.Manager.mapper.stock.StockMovementMapper;
import com.stockup.StockUp.Manager.model.stock.Stock;
import com.stockup.StockUp.Manager.model.stock.StockMovement;
import com.stockup.StockUp.Manager.repository.stock.StockMovementRepository;
import com.stockup.StockUp.Manager.repository.stock.StockRepository;
import com.stockup.StockUp.Manager.service.stock.IStockMovementService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StockMovementService implements IStockMovementService {
	
	private final StockMovementRepository movementRepository;
	private final StockRepository stockRepository;
	private final StockMovementMapper movementMapper;
	
	@Override
	@Transactional
	public StockMovementResponseDTO registerMovement(StockMovementRequestDTO dto) {
		Stock stock = stockRepository.findByProductId(dto.productId())
			.orElseThrow(() -> new ResourceNotFoundException("Stock not found for product " + dto.productId()));
		
		if (dto.type() == StockMovementType.INCREASE) {
			stock.setQuantity(stock.getQuantity() + dto.quantity());
		} else {
			if (stock.getQuantity() < dto.quantity()) {
				throw new IllegalArgumentException("Insufficient stock.");
			}
			stock.setQuantity(stock.getQuantity() - dto.quantity());
		}
		
		StockMovement movement = movementMapper.toEntity(dto);
		movement.setStock(stock);
		movementRepository.save(movement);
		
		return movementMapper.toResponseDTO(movement);
	}
	
	@Override
	public StockMovementResponseDTO getById(UUID id) {
		StockMovement movement = movementRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Stock movement not found: " + id));
		return movementMapper.toResponseDTO(movement);
	}
	
	@Override
	public List<StockMovementResponseDTO> listAll() {
		return movementRepository.findAll().stream()
			.map(movementMapper::toResponseDTO)
			.toList();
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		if (!movementRepository.existsById(id)) {
			throw new ResourceNotFoundException("Movement not found: " + id);
		}
		movementRepository.deleteById(id);
	}
}
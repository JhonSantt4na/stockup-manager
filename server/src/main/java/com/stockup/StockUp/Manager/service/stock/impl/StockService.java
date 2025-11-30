package com.stockup.StockUp.Manager.service.stock.impl;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import com.stockup.StockUp.Manager.mapper.stock.StockMapper;
import com.stockup.StockUp.Manager.model.stock.Stock;
import com.stockup.StockUp.Manager.repository.stock.StockRepository;
import com.stockup.StockUp.Manager.service.stock.IStockService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StockService implements IStockService {
	
	private final StockRepository stockRepository;
	private final StockMapper stockMapper;
	
	@Override
	@Transactional
	public StockResponseDTO create(StockRequestDTO dto) {
		Stock entity = stockMapper.toEntity(dto);
		entity = stockRepository.save(entity);
		return stockMapper.toResponseDTO(entity);
	}
	
	@Override
	@Transactional
	public StockResponseDTO update(UUID id, StockRequestDTO dto) {
		Stock existing = stockRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + id));
		
		stockMapper.updateEntityFromDTO(dto, existing);
		stockRepository.save(existing);
		
		return stockMapper.toResponseDTO(existing);
	}
	
	@Override
	public StockResponseDTO getById(UUID id) {
		Stock entity = stockRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Stock not found: " + id));
		
		return stockMapper.toResponseDTO(entity);
	}
	
	@Override
	public List<StockResponseDTO> listAll() {
		return stockRepository.findAll()
			.stream()
			.map(stockMapper::toResponseDTO)
			.toList();
	}
	
	@Override
	@Transactional
	public void delete(UUID id) {
		if (!stockRepository.existsById(id)) {
			throw new ResourceNotFoundException("Stock not found: " + id);
		}
		stockRepository.deleteById(id);
	}

}
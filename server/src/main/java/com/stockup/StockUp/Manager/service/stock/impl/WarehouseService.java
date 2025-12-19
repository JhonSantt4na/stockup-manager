package com.stockup.StockUp.Manager.service.stock.impl;

import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;
import com.stockup.StockUp.Manager.exception.BusinessException;
import com.stockup.StockUp.Manager.mapper.stock.WarehouseMapper;
import com.stockup.StockUp.Manager.model.stock.Warehouse;
import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.repository.stock.WarehouseRepository;
import com.stockup.StockUp.Manager.repository.stock.StockRepository;
import com.stockup.StockUp.Manager.service.stock.IWarehouseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService implements IWarehouseService {
	
	private final WarehouseRepository repository;
	private final StockRepository stockRepository;
	private final WarehouseMapper mapper;
	
	@Override
	public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO dto) {
		log.info("Creating a new warehouse. Payload={}", dto);
		
		if (repository.existsByName(dto.name())) {
			log.warn("Warehouse creation aborted. Name already exists: {}", dto.name());
			throw new BusinessException("A warehouse with this name already exists");
		}
		
		Warehouse warehouse = mapper.toEntity(dto);
		
		repository.save(warehouse);
		
		log.info("Warehouse successfully created. id={}", warehouse.getId());
		return mapper.toDTO(warehouse);
	}
	
	@Override
	public WarehouseResponseDTO updateWarehouse(UUID id, WarehouseRequestDTO dto) {
		log.info("Updating warehouse. id={}, payload={}", id, dto);
		
		Warehouse warehouse = repository.findById(id)
			.orElseThrow(() -> {
				log.warn("Warehouse updatePaymentMethod failed. Warehouse not found. id={}", id);
				return new EntityNotFoundException("Warehouse not found");
			});
		
		mapper.updateEntityFromDTO(dto, warehouse);
		
		repository.save(warehouse);
		
		log.info("Warehouse successfully updated. id={}", id);
		return mapper.toDTO(warehouse);
	}
	
	@Override
	public WarehouseResponseDTO getWarehouseById(UUID id) {
		log.debug("Fetching warehouse by id={}", id);
		
		Warehouse warehouse = repository.findById(id)
			.orElseThrow(() -> {
				log.warn("Warehouse fetch failed. Warehouse not found. id={}", id);
				return new EntityNotFoundException("Warehouse not found");
			});
		
		return mapper.toDTO(warehouse);
	}
	
	@Override
	public Page<WarehouseResponseDTO> listAllWarehouse(Integer page, Integer size, WarehouseType type) {
		log.debug("Listing warehouses. page={}, size={}, typeFilter={}", page, size, type);
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Warehouse> result;
		
		if (type != null) {
			log.debug("Applying WarehouseType filter: {}", type);
			result = repository.findByType(type, pageable);
		} else {
			result = repository.findAll(pageable);
		}
		
		return result.map(mapper::toDTO);
	}
	
	@Override
	public void deleteWarehouse(UUID id) {
		log.info("Attempting to deleteCashMovement warehouse. id={}",id);
		
		Warehouse warehouse = repository.findById(id)
			.orElseThrow(() -> {
				log.warn("Warehouse deleteCashMovement failed. Not found. id={}", id);
				return new EntityNotFoundException("Warehouse not found");
			});
		
		boolean hasStock = stockRepository.existsByWarehouse(warehouse);
		
		if (hasStock) {
			log.warn("Warehouse deletion blocked. Stock records found for warehouse id={}", id);
			throw new BusinessException("This warehouse cannot be deleted because it has associated stock records");
		}
		
		repository.delete(warehouse);
		
		log.info("Warehouse successfully deleted. id={}", id);
	}
}
package com.stockup.StockUp.Manager.service.procurement.impl;

import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseOrder.PurchaseOrderResponseDTO;
import com.stockup.StockUp.Manager.mapper.procurement.PurchaseOrderMapper;
import com.stockup.StockUp.Manager.model.procurement.PurchaseOrder;
import com.stockup.StockUp.Manager.repository.procurement.PurchaseOrderRepository;
import com.stockup.StockUp.Manager.service.procurement.IPurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService implements IPurchaseOrderService {
	
	private final PurchaseOrderRepository repository;
	private final PurchaseOrderMapper mapper;
	
	@Override
	public PurchaseOrderResponseDTO create(PurchaseOrderRequestDTO dto) {
		
		if (repository.existsByOrderNumber(dto.orderNumber())) {
			throw new IllegalArgumentException("Já existe um pedido de compra com este número.");
		}
		
		PurchaseOrder order = mapper.toEntity(dto);
		return mapper.toResponse(repository.save(order));
	}
	
	@Override
	public PurchaseOrderResponseDTO update(UUID id, PurchaseOrderRequestDTO dto) {
		
		PurchaseOrder order = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado."));
		
		mapper.update(order, dto);
		return mapper.toResponse(repository.save(order));
	}
	
	@Override
	public void delete(UUID id) {
		repository.deleteById(id);
	}
	
	@Override
	public PurchaseOrderResponseDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toResponse)
			.orElseThrow(() -> new EntityNotFoundException("Pedido de compra não encontrado."));
	}
	
	@Override
	public List<PurchaseOrderResponseDTO> findAll() {
		return repository.findAll().stream().map(mapper::toResponse).toList();
	}
	
	@Override
	public Page<PurchaseOrderResponseDTO> list(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toResponse);
	}
}
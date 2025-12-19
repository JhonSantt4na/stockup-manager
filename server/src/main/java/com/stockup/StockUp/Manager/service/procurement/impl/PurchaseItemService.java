package com.stockup.StockUp.Manager.service.procurement.impl;

import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.PurchaseItem.PurchaseItemResponseDTO;
import com.stockup.StockUp.Manager.mapper.procurement.PurchaseItemMapper;
import com.stockup.StockUp.Manager.model.procurement.PurchaseItem;
import com.stockup.StockUp.Manager.repository.procurement.PurchaseItemRepository;
import com.stockup.StockUp.Manager.service.procurement.IPurchaseItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseItemService implements IPurchaseItemService {
	
	private final PurchaseItemRepository repository;
	private final PurchaseItemMapper mapper;
	
	@Override
	public PurchaseItemResponseDTO createPurchaseItem(PurchaseItemRequestDTO dto) {
		PurchaseItem item = mapper.toEntity(dto);
		return mapper.toResponse(repository.save(item));
	}
	
	@Override
	public PurchaseItemResponseDTO updatePurchaseItem(UUID id, PurchaseItemRequestDTO dto) {
		
		PurchaseItem item = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Item de compra não encontrado."));
		
		mapper.update(item, dto);
		return mapper.toResponse(repository.save(item));
	}
	
	@Override
	public void deletePurchaseItem(UUID id) {
		repository.deleteById(id);
	}
	
	@Override
	public PurchaseItemResponseDTO findPurchaseItemById(UUID id) {
		return repository.findById(id)
			.map(mapper::toResponse)
			.orElseThrow(() -> new EntityNotFoundException("Item de compra não encontrado."));
	}
	
	@Override
	public List<PurchaseItemResponseDTO> findPurchaseItemByPurchaseOrder(UUID purchaseOrderId) {
		return repository.findByPurchaseOrderId(purchaseOrderId)
			.stream()
			.map(mapper::toResponse)
			.toList();
	}
}
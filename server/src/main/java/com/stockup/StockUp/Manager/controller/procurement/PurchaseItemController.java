package com.stockup.StockUp.Manager.controller.procurement;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.procurement.docs.PurchaseItemControllerDocs;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.PurchaseItem.PurchaseItemResponseDTO;
import com.stockup.StockUp.Manager.service.procurement.IPurchaseItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/supply/purchase-items")
@RequiredArgsConstructor
public class PurchaseItemController implements PurchaseItemControllerDocs {
	
	private final IPurchaseItemService service;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<PurchaseItemResponseDTO> createPurchaseItem(@RequestBody PurchaseItemRequestDTO dto) {
		PurchaseItemResponseDTO response = service.create(dto);
		AuditLogger.log("PURCHASE_ITEM_CREATE", getCurrentUser(), "SUCCESS", "Item created");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<PurchaseItemResponseDTO> updatePurchaseItem(@PathVariable UUID id, @RequestBody PurchaseItemRequestDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<PurchaseItemResponseDTO> findPurchaseItemById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<PurchaseItemResponseDTO>> findItemsByOrder(@PathVariable UUID orderId) {
		return ResponseEntity.ok(service.findByPurchaseOrder(orderId));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePurchaseItem(@PathVariable UUID id) {
		service.delete(id);
		AuditLogger.log("PURCHASE_ITEM_DELETE", getCurrentUser(), "SUCCESS", "Item deleted: " + id);
		return ResponseEntity.noContent().build();
	}
}

package com.stockup.StockUp.Manager.controller.stock;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.stock.docs.WarehouseControllerDocs;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.stock.impl.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseControllerDocs {
	
	private final WarehouseService warehouseService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO dto) {
		try {
			WarehouseResponseDTO response = warehouseService.create(dto);
			AuditLogger.log("WAREHOUSE_CREATE", getCurrentUser(), "SUCCESS",
				"Warehouse created: " + dto.name());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("WAREHOUSE_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("WAREHOUSE_CREATE", getCurrentUser(), "FAILED",
				"Error creating warehouse: " + e.getMessage());
			throw new RuntimeException("Error creating warehouse", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<WarehouseResponseDTO> updateWarehouse(
		@PathVariable UUID id,
		@Valid @RequestBody WarehouseRequestDTO dto) {
		try {
			WarehouseResponseDTO response = warehouseService.update(id, dto);
			AuditLogger.log("WAREHOUSE_UPDATE", getCurrentUser(), "SUCCESS",
				"Warehouse updated: " + dto.name());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			AuditLogger.log("WAREHOUSE_UPDATE", getCurrentUser(), "FAILED",
				"Error updating warehouse: " + e.getMessage());
			throw new RuntimeException("Error updating warehouse", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<WarehouseResponseDTO> getWarehouseById(@PathVariable UUID id) {
		WarehouseResponseDTO dto = warehouseService.getById(id);
		return ResponseEntity.ok(dto);
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<WarehouseResponseDTO>> listWarehouses() {
		return ResponseEntity.ok(warehouseService.listAll());
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWarehouse(@PathVariable UUID id) {
		try {
			warehouseService.delete(id);
			AuditLogger.log("WAREHOUSE_DELETE", getCurrentUser(), "SUCCESS",
				"Warehouse deleted: " + id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("WAREHOUSE_DELETE", getCurrentUser(), "FAILED",
				"Error deleting warehouse: " + e.getMessage());
			throw new RuntimeException("Error deleting warehouse", e);
		}
	}
}

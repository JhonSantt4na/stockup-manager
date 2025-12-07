package com.stockup.StockUp.Manager.controller.stock;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.stock.docs.StockControllerDocs;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
import com.stockup.StockUp.Manager.service.stock.IStockService;
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
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController implements StockControllerDocs {
	
	private final IStockService stockService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO dto) {
		try {
			StockResponseDTO response = stockService.create(dto);
			AuditLogger.log("STOCK_CREATE", getCurrentUser(), "SUCCESS",
				"Stock created for product: " + dto.productId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			AuditLogger.log("STOCK_CREATE", getCurrentUser(), "FAILED",
				"Error creating stock: " + e.getMessage());
			throw new RuntimeException("Error creating stock", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<StockResponseDTO> updateStock(
		@PathVariable UUID id,
		@Valid @RequestBody StockRequestDTO dto) {
		
		try {
			StockResponseDTO response = stockService.update(id, dto);
			AuditLogger.log("STOCK_UPDATE", getCurrentUser(), "SUCCESS",
				"Stock updated ID=" + id);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			AuditLogger.log("STOCK_UPDATE", getCurrentUser(), "FAILED",
				"Error updating stock: " + e.getMessage());
			throw new RuntimeException("Error updating stock", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<StockResponseDTO> getStockById(@PathVariable UUID id) {
		return ResponseEntity.ok(stockService.getById(id));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<StockResponseDTO>> listAllStock() {
		return ResponseEntity.ok(stockService.listAll());
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStock(@PathVariable UUID id) {
		try {
			stockService.delete(id);
			AuditLogger.log("STOCK_DELETE", getCurrentUser(), "SUCCESS",
				"Stock deleted ID=" + id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("STOCK_DELETE", getCurrentUser(), "FAILED",
				"Error deleting stock: " + e.getMessage());
			throw new RuntimeException("Error deleting stock", e);
		}
	}
}


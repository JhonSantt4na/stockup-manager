package com.stockup.StockUp.Manager.controller.stock;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.stock.docs.StockMovementControllerDocs;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;
import com.stockup.StockUp.Manager.service.stock.IStockMovementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/stock/movements")
@RequiredArgsConstructor
public class StockMovementController implements StockMovementControllerDocs {
	
	private final IStockMovementService movementService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
	@PostMapping("/createCashMovement")
	public ResponseEntity<StockMovementResponseDTO> createStockMovements(
		@Valid @RequestBody StockMovementRequestDTO dto) {
		try {
			StockMovementResponseDTO response = movementService.registerStockMovement(dto);
			AuditLogger.log("STOCK_MOVEMENT_CREATE", getCurrentUser(), "SUCCESS",
				"Movement created: product=" + dto.productId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			AuditLogger.log("STOCK_MOVEMENT_CREATE", getCurrentUser(), "FAILED",
				"Error creating movement: " + e.getMessage());
			throw new RuntimeException("Error creating stock movement", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<StockMovementResponseDTO> getStockMovementsById(@PathVariable @NotNull UUID id) {
		return ResponseEntity.ok(movementService.getStockMovementById(id));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<StockMovementResponseDTO>> listStockMovements() {
		return ResponseEntity.ok(movementService.listAllStockMovement());
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStockMovements(@PathVariable @NotNull UUID id) {
		try {
			movementService.deleteStockMovement(id);
			AuditLogger.log("STOCK_MOVEMENT_DELETE", getCurrentUser(), "SUCCESS",
				"Movement deleted ID=" + id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			AuditLogger.log("STOCK_MOVEMENT_DELETE", getCurrentUser(), "FAILED",
				"Error deleting movement: " + e.getMessage());
			throw new RuntimeException("Error deleting movement", e);
		}
	}
}

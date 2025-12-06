package com.stockup.StockUp.Manager.controller.stock.docs;

import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockMovementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

public interface StockMovementControllerDocs {
	
	@Operation(
		summary = "Registrar movimentação de estoque",
		description = "Cria uma movimentação de entrada, saída ou ajuste no estoque.",
		tags = {"Stock Movements"}
	)
	@PostMapping
	ResponseEntity<StockMovementResponseDTO> createMovement(@Valid @RequestBody StockMovementRequestDTO dto);
	
	@Operation(
		summary = "Buscar movimentação por ID",
		tags = {"Stock Movements"}
	)
	@GetMapping("/{id}")
	ResponseEntity<StockMovementResponseDTO> getMovementById(
		@Parameter(description = "ID da movimentação") @PathVariable UUID id);
	
	@Operation(
		summary = "Listar todas IPurchaseOrderService movimentações",
		tags = {"Stock Movements"}
	)
	@GetMapping
	ResponseEntity<List<StockMovementResponseDTO>> listMovements();
	
	@Operation(
		summary = "Excluir movimentação (lógica)",
		tags = {"Stock Movements"}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteMovement(
		@Parameter(description = "ID da movimentação") @PathVariable UUID id);
}

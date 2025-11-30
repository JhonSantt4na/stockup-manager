package com.stockup.StockUp.Manager.controller.stock.docs;


import com.stockup.StockUp.Manager.dto.Stock.stock.StockRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.stock.StockResponseDTO;
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

public interface StockControllerDocs {
	
	@Operation(
		summary = "Criar registro de estoque",
		description = "Cria um registro de estoque inicial para um produto em um warehouse.",
		tags = {"Stock"}
	)
	@PostMapping
	ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO dto);
	
	@Operation(
		summary = "Atualizar dados de estoque",
		description = "Permite ajustar quantidade mínima/máxima e informações administrativas.",
		tags = {"Stock"}
	)
	@PutMapping("/{id}")
	ResponseEntity<StockResponseDTO> updateStock(
		@Parameter(description = "ID do estoque") @PathVariable UUID id,
		@Valid @RequestBody StockRequestDTO dto);
	
	@Operation(
		summary = "Buscar estoque por ID",
		tags = {"Stock"}
	)
	@GetMapping("/{id}")
	ResponseEntity<StockResponseDTO> getStockById(
		@Parameter(description = "ID do estoque") @PathVariable UUID id);
	
	@Operation(
		summary = "Listar todos os estoques",
		tags = {"Stock"}
	)
	@GetMapping
	ResponseEntity<List<StockResponseDTO>> listAllStock();
	
	@Operation(
		summary = "Excluir registro de estoque",
		tags = {"Stock"}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteStock(
		@Parameter(description = "ID do estoque") @PathVariable UUID id);
}


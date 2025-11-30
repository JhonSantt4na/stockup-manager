package com.stockup.StockUp.Manager.controller.stock.docs;

import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;
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

public interface WarehouseControllerDocs {
	
	@Operation(
		summary = "Criar um novo depósito/almoxarifado",
		description = "Cria um novo warehouse usado para controle de estoque no PDV.",
		tags = {"Warehouses"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Warehouse criado com sucesso",
				content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "409", description = "Warehouse já existente", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO dto);
	
	@Operation(
		summary = "Atualizar um warehouse",
		description = "Atualiza os dados de um depósito existente.",
		tags = {"Warehouses"}
	)
	@PutMapping("/{id}")
	ResponseEntity<WarehouseResponseDTO> updateWarehouse(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id,
		@Valid @RequestBody WarehouseRequestDTO dto);
	
	@Operation(
		summary = "Buscar warehouse por ID",
		tags = {"Warehouses"}
	)
	@GetMapping("/{id}")
	ResponseEntity<WarehouseResponseDTO> getWarehouseById(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id);
	
	@Operation(
		summary = "Listar todos os warehouses",
		tags = {"Warehouses"}
	)
	@GetMapping
	ResponseEntity<List<WarehouseResponseDTO>> listWarehouses();
	
	@Operation(
		summary = "Excluir um warehouse",
		description = "Exclusão lógica do depósito.",
		tags = {"Warehouses"}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteWarehouse(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id);
}


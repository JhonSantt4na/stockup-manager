package com.stockup.StockUp.Manager.controller.stock.docs;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseRequestDTO;
import com.stockup.StockUp.Manager.dto.Stock.warehouse.WarehouseResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface WarehouseControllerDocs {
	
	@Operation(
		summary = "Criar um novo depósito/almoxarifado",
		description = "Cria um novo warehouse usado para controle de estoque no sistema.",
		tags = {"Warehouses"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Warehouse criado com sucesso",
				content = @Content(schema = @Schema(implementation = WarehouseResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "409", description = "Warehouse já existente", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<WarehouseResponseDTO> createWarehouse(
		@Valid @RequestBody WarehouseRequestDTO dto);
	
	
	@Operation(
		summary = "Atualizar um warehouse",
		description = "Atualiza os dados de um warehouse existente.",
		tags = {"Warehouses"}
	)
	@PutMapping("/{id}")
	ResponseEntity<WarehouseResponseDTO> updateWarehouse(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id,
		@Valid @RequestBody WarehouseRequestDTO dto);
	
	
	@Operation(
		summary = "Buscar warehouse por ID",
		description = "Retorna os dados de um warehouse conforme seu ID.",
		tags = {"Warehouses"}
	)
	@GetMapping("/{id}")
	ResponseEntity<WarehouseResponseDTO> getWarehouseById(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id);
	
	
	@Operation(
		summary = "Listar depósitos com paginação e filtro por tipo",
		description = """
                    Lista todos os warehouses com suporte a:
                    • Paginação (page / size)
                    • Filtro opcional por tipo de warehouse (STORE, STOCKROOM, DISTRIBUTION)
                    """,
		tags = {"Warehouses"}
	)
	@GetMapping
	ResponseEntity<Page<WarehouseResponseDTO>> listWarehouses(
		@Parameter(description = "Número da página (0 default)")
		@RequestParam(defaultValue = "0") Integer page,
		
		@Parameter(description = "Quantidade de itens por página (10 default)")
		@RequestParam(defaultValue = "10") Integer size,
		
		@Parameter(description = "Filtrar por tipo de warehouse (opcional)")
		@RequestParam(required = false) WarehouseType type
	);
	
	@Operation(
		summary = "Excluir um warehouse",
		description = "Exclusão do warehouse, somente se não estiver em uso (estoque associado).",
		tags = {"Warehouses"}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteWarehouse(
		@Parameter(description = "ID do warehouse") @PathVariable UUID id);
}

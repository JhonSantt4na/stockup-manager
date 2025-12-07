package com.stockup.StockUp.Manager.controller.supply.docs;

import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin - Supplier", description = "Gerenciamento de Fornecedores")
public interface SupplierControllerDocs {
	
	@Operation(
		summary = "Criar novo fornecedor",
		description = "Cria um novo fornecedor no sistema.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Fornecedor criado",
				content = @Content(schema = @Schema(implementation = SupplierResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "409", description = "Fornecedor já cadastrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno")
		}
	)
	@PostMapping("/create")
	ResponseEntity<SupplierResponseDTO> createSupplier(@Valid @RequestBody SupplierRequestDTO dto);
	
	@Operation(
		summary = "Atualizar fornecedor",
		description = "Atualiza os dados de um fornecedor existente.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Fornecedor atualizado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
		}
	)
	@PutMapping("/update/{id}")
	ResponseEntity<SupplierResponseDTO> updateSupplier(@PathVariable UUID id, @Valid @RequestBody SupplierRequestDTO dto);
	
	@Operation(
		summary = "Buscar fornecedor por ID",
		description = "Retorna os dados de um fornecedor pelo seu ID.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
			@ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<SupplierResponseDTO> findSupplierById(@PathVariable UUID id);
	
	@Operation(
		summary = "Excluir fornecedor",
		description = "Remove um fornecedor do sistema.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Fornecedor excluído")
		}
	)
	@DeleteMapping("/delete/{id}")
	ResponseEntity<Void> deleteSupplier(@PathVariable UUID id);
	
	@Operation(
		summary = "Listar fornecedores",
		description = "Lista todos os fornecedores com paginação.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista retornada")
		}
	)
	@GetMapping("/list")
	ResponseEntity<Page<SupplierResponseDTO>> listSuppliers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "name,asc") String[] sort
	);
}

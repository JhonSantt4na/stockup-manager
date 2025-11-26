package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.dto.sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandResponseDTO;
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

@Tag(name = "Admin - Brand", description = "Gerenciamento de Marcas")
public interface BrandControllerDocs {
	
	@Operation(
		summary = "Criar nova marca",
		description = "Cria uma nova marca. Acesso restrito a administradores.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Marca criada", content = @Content(schema = @Schema(implementation = BrandResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "409", description = "Marca já cadastrada"),
			@ApiResponse(responseCode = "500", description = "Erro interno")
		}
	)
	@PostMapping("/create")
	ResponseEntity<BrandResponseDTO> create(@Valid @RequestBody BrandRequestDTO dto);
	
	@Operation(
		summary = "Atualizar marca",
		description = "Atualiza uma marca existente.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Marca atualizada"),
			@ApiResponse(responseCode = "404", description = "Marca não encontrada")
		}
	)
	@PutMapping("/update/{id}")
	ResponseEntity<BrandResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody BrandRequestDTO dto);
	
	@Operation(
		summary = "Buscar marca por ID",
		description = "Retorna uma marca pelo seu ID.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Marca encontrada"),
			@ApiResponse(responseCode = "404", description = "Marca não encontrada")
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<BrandResponseDTO> findById(@PathVariable UUID id);
	
	@Operation(
		summary = "Excluir marca",
		description = "Exclui uma marca pelo seu ID.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Marca excluída")
		}
	)
	@DeleteMapping("/delete/{id}")
	ResponseEntity<Void> delete(@PathVariable UUID id);
	
	@Operation(
		summary = "Listar marcas",
		description = "Lista todas as marcas com paginação.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista retornada")
		}
	)
	@GetMapping("/list")
	ResponseEntity<Page<BrandResponseDTO>> listBrands(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "name,asc") String[] sort
	);
}

package com.stockup.StockUp.Manager.controller.catalog.docs;

import com.stockup.StockUp.Manager.dto.Sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.category.CategoryResponseDTO;
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

public interface CategoryControllerDocs {
	
	@Operation(
		summary = "Criar uma nova categoria",
		description = "Cria uma categoria com nome e descrição informados.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Categoria criada com sucesso",
				content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
			),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Categoria já existe", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto);
	
	
	@Operation(
		summary = "Atualizar uma categoria",
		description = "Atualiza uma categoria existente pelo ID.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Categoria atualizada",
				content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
			),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
			@ApiResponse(responseCode = "409", description = "Nome já utilizado", content = @Content)
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<CategoryResponseDTO> updateCategory(
		@Parameter(description = "ID da categoria") @PathVariable UUID id,
		@Valid @RequestBody CategoryRequestDTO dto
	);
	
	
	@Operation(
		summary = "Buscar categoria pelo nome",
		description = "Retorna categoria correspondente ao nome informado.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Categoria encontrada",
				content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
		}
	)
	@GetMapping("/by-name/{name}")
	ResponseEntity<CategoryResponseDTO> getCategoryByName(
		@Parameter(description = "Nome da categoria") @PathVariable String name
	);
	
	
	@Operation(
		summary = "Excluir categoria",
		description = "Remove logicamente uma categoria.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Categoria excluída"),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteCategory(@PathVariable UUID id);
	
	
	@Operation(
		summary = "Listar categorias",
		description = "Retorna lista paginada de categorias (todas).",
		tags = {"Categorias"}
	)
	@GetMapping
	ResponseEntity<Page<CategoryResponseDTO>> listCategories(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort
	);
	
	
	@Operation(
		summary = "Listar categorias ativas",
		description = "Retorna lista paginada das categorias ativas.",
		tags = {"Categorias"}
	)
	@GetMapping("/active")
	ResponseEntity<Page<CategoryResponseDTO>> listActiveCategories(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort
	);
}

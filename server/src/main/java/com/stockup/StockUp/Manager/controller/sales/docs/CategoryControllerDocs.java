package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.dto.sales.category.CategoryRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.category.CategoryResponseDTO;
import com.stockup.StockUp.Manager.model.sales.Category;
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
		description = "Cria uma nova categoria com o nome e descrição informados. Apenas administradores podem criar categorias.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
				content = @Content(schema = @Schema(implementation = Category.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Categoria já existe", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequestDTO dto);
	
	
	@Operation(
		summary = "Atualizar uma categoria",
		description = "Atualiza os dados de uma categoria existente com base em seu ID.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
				content = @Content(schema = @Schema(implementation = Category.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
			@ApiResponse(responseCode = "409", description = "Nome da categoria já existe", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<Category> updateCategory(
		@Parameter(description = "ID da categoria") @PathVariable UUID id,
		@Valid @RequestBody CategoryRequestDTO dto);
	
	
	@Operation(
		summary = "Buscar categoria pelo nome",
		description = "Retorna os dados de uma categoria com base no nome informado.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Categoria encontrada",
				content = @Content(schema = @Schema(implementation = Category.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@GetMapping("/by-name/{name}")
	ResponseEntity<Category> getPermissionByCategoryName(
		@Parameter(description = "Nome da categoria") @PathVariable String name);
	
	
	@Operation(
		summary = "Excluir uma categoria",
		description = "Remove logicamente uma categoria com base no seu ID. Apenas administradores podem executar esta ação.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Categoria não encontrada", content = @Content),
			@ApiResponse(responseCode = "409", description = "Categoria já estava desativada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteCategory(
		@Parameter(description = "ID da categoria") @PathVariable UUID id);
	
	
	@Operation(
		summary = "Listar todas as categorias",
		description = "Retorna uma lista paginada e ordenável de todas as categorias, ativas ou não.",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de categorias obtida com sucesso",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = CategoryResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
		}
	)
	@GetMapping
	ResponseEntity<Page<CategoryResponseDTO>> listCategory(
		@Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: name,asc") @RequestParam(defaultValue = "createdAt,desc") String[] sort);
	
	
	@Operation(
		summary = "Listar apenas categorias ativas",
		description = "Retorna uma lista paginada e ordenável de categorias ativas (não excluídas e habilitadas).",
		tags = {"Categorias"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de categorias ativas obtida com sucesso",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = CategoryResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
		}
	)
	@GetMapping("/active")
	ResponseEntity<Page<CategoryResponseDTO>> getAllPermissionsIsActive(
		@Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: name,asc") @RequestParam(defaultValue = "createdAt,desc") String[] sort);
}

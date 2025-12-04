package com.stockup.StockUp.Manager.controller.catalog.docs;

import com.stockup.StockUp.Manager.dto.Sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductUpdateDTO;
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

public interface ProductControllerDocs {
	
	@Operation(
		summary = "Criar um novo produto",
		description = "Cria um novo produto com informações completas de categoria e perfil fiscal. Apenas administradores podem criar produtos.",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
				content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Produto com mesmo SKU já existe", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto);
	
	
	@Operation(
		summary = "Atualizar um produto existente",
		description = "Atualiza as informações de um produto com base no seu ID. Permite atualização de dados fiscais, categoria e status.",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
				content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<ProductResponseDTO> updateProduct(
		@Parameter(description = "ID do produto") @PathVariable UUID id,
		@Valid @RequestBody ProductUpdateDTO dto);
	
	
	@Operation(
		summary = "Buscar produto por nome",
		description = "Retorna as informações de um produto com base no nome informado (busca parcial, insensível a maiúsculas/minúsculas).",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Produto encontrado",
				content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@GetMapping("/by-name/{name}")
	ResponseEntity<ProductResponseDTO> getProductByName(
		@Parameter(description = "Nome do produto") @PathVariable String name);
	
	
	@Operation(
		summary = "Buscar produto por SKU",
		description = "Retorna as informações completas de um produto com base no código SKU (identificador interno).",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Produto encontrado",
				content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@GetMapping("/by-sku/{sku}")
	ResponseEntity<ProductResponseDTO> getProductBySku(
		@Parameter(description = "SKU do produto") @PathVariable String sku);
	
	
	@Operation(
		summary = "Excluir (desativar) um produto",
		description = "Remove logicamente um produto com base em seu ID. Apenas administradores podem executar esta ação.",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Produto já estava desativado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteProduct(@Parameter(description = "ID do produto") @PathVariable UUID id);
	
	
	@Operation(
		summary = "Listar todos os produtos",
		description = "Retorna uma lista paginada e ordenável de todos os produtos, ativos ou não.",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ProductSummaryDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
		}
	)
	@GetMapping
	ResponseEntity<Page<ProductSummaryDTO>> listProducts(
		@Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: name,asc") @RequestParam(defaultValue = "createdAt,desc") String[] sort);
	
	
	@Operation(
		summary = "Listar apenas produtos ativos",
		description = "Retorna uma lista paginada e ordenável de produtos ativos, disponíveis para venda.",
		tags = {"Produtos"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de produtos ativos obtida com sucesso",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ProductSummaryDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
		}
	)
	@GetMapping("/active")
	ResponseEntity<Page<ProductSummaryDTO>> listActiveProducts(
		@Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: name,asc") @RequestParam(defaultValue = "createdAt,desc") String[] sort);
}


package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
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

public interface CustomerControllerDocs {
	
	@Operation(
		summary = "Criar um novo cliente",
		description = "Cria um cliente com nome, CPF/CNPJ, contato e endereço opcional.",
		tags = {"Clientes"},
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Cliente criado com sucesso",
				content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))
			),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto);
	
	
	@Operation(
		summary = "Atualizar um cliente",
		description = "Atualiza os dados de um cliente pelo ID.",
		tags = {"Clientes"},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Cliente atualizado",
				content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))
			),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<CustomerResponseDTO> update(
		@Parameter(description = "ID do cliente") @PathVariable UUID id,
		@Valid @RequestBody CustomerRequestDTO dto
	);
	
	
	@Operation(
		summary = "Buscar cliente por ID",
		description = "Retorna os dados completos do cliente pelo ID.",
		tags = {"Clientes"},
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Cliente encontrado",
				content = @Content(schema = @Schema(implementation = CustomerResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<CustomerResponseDTO> getById(
		@Parameter(description = "ID do cliente") @PathVariable UUID id
	);
	
	
	@Operation(
		summary = "Listar clientes",
		description = "Retorna uma lista paginada de clientes (todos).",
		tags = {"Clientes"}
	)
	@GetMapping
	ResponseEntity<Page<CustomerSummaryDTO>> list(
		@Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação") @RequestParam(defaultValue = "createdAt,desc") String[] sort
	);
	
	
	@Operation(
		summary = "Excluir cliente",
		description = "Realiza exclusão lógica (soft delete).",
		tags = {"Clientes"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Cliente excluído"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable UUID id);
	
	
	@Operation(
		summary = "Reativar cliente",
		description = "Reverte o soft delete, habilitando o cliente novamente.",
		tags = {"Clientes"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Cliente reativado"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content)
		}
	)
	@PostMapping("/{id}/enable")
	ResponseEntity<Void> enable(@PathVariable UUID id);
}

package com.stockup.StockUp.Manager.controller.others.Docs;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
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

@Tag(name = "Addresses", description = "Gerenciamento de endereços de clientes")
public interface AddressControllerDocs {
	
	@Operation(
		summary = "Criar endereço",
		description = "Cria um novo endereço para um cliente.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Endereço criado",
				content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "409", description = "Endereço já cadastrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno")
		}
	)
	@PostMapping
	ResponseEntity<AddressResponseDTO> createAddress(
		@Valid @RequestBody AddressRequestDTO dto
	);
	
	@Operation(
		summary = "Atualizar endereço",
		description = "Atualiza um endereço existente.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Endereço atualizado",
				content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Endereço não encontrado")
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<AddressResponseDTO> updateAddress(
		@PathVariable UUID id,
		@Valid @RequestBody AddressRequestDTO dto
	);
	
	@Operation(
		summary = "Buscar endereço por ID",
		description = "Retorna um endereço pelo seu identificador único.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Endereço encontrado",
				content = @Content(schema = @Schema(implementation = AddressResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Endereço não encontrado")
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<AddressResponseDTO> findAddressById(@PathVariable UUID id);
	
	@Operation(
		summary = "Listar endereços",
		description = "Lista todos os endereços com paginação e ordenação.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista retornada",
				content = @Content(schema = @Schema(implementation = Page.class)))
		}
	)
	@GetMapping
	ResponseEntity<Page<AddressSummaryDTO>> findAllAddress(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort
	);
	
	@Operation(
		summary = "Desativar endereço",
		description = "Realiza o soft delete do endereço.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Endereço desativado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Endereço não encontrado")
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> softDeleteAddress(@PathVariable UUID id);
	
	@Operation(
		summary = "Reativar endereço",
		description = "Reativa um endereço previamente desativado.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Endereço reativado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Endereço não encontrado")
		}
	)
	@PatchMapping("/{id}/enableAddress")
	ResponseEntity<Void> enableAddress(@PathVariable UUID id);
}

package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileUpdateDTO;
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

public interface  TaxProfileControllerDocs {
	
	@Operation(
		summary = "Criar um novo perfil fiscal",
		description = "Cria um novo conjunto de informações fiscais com CST, NCM, CFOP e alíquotas padrão. Apenas administradores podem criar perfis fiscais.",
		tags = {"Perfis Fiscais"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Perfil fiscal criado com sucesso",
				content = @Content(schema = @Schema(implementation = TaxProfileResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Perfil fiscal já existe", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PostMapping
	ResponseEntity<TaxProfileResponseDTO> createTaxProfile(@Valid @RequestBody TaxProfileRequestDTO dto);
	
	@Operation(
		summary = "Atualizar um perfil fiscal existente",
		description = "Atualiza as informações fiscais de um perfil existente com base em seu ID.",
		tags = {"Perfis Fiscais"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Perfil fiscal atualizado com sucesso",
				content = @Content(schema = @Schema(implementation = TaxProfileResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Perfil fiscal não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@PutMapping("/{id}")
	ResponseEntity<TaxProfileResponseDTO> updateTaxProfile(
		@Parameter(description = "ID do perfil fiscal") @PathVariable UUID id,
		@Valid @RequestBody TaxProfileUpdateDTO dto);
	
	@Operation(
		summary = "Buscar perfil fiscal por ID",
		description = "Retorna as informações completas de um perfil fiscal com base em seu ID.",
		tags = {"Perfis Fiscais"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Perfil fiscal encontrado",
				content = @Content(schema = @Schema(implementation = TaxProfileResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Perfil fiscal não encontrado", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
		}
	)
	@GetMapping("/{id}")
	ResponseEntity<TaxProfileResponseDTO> getTaxProfileById(
		@Parameter(description = "ID do perfil fiscal") @PathVariable UUID id);
	
	@Operation(
		summary = "Listar todos os perfis fiscais",
		description = "Retorna a lista completa de perfis fiscais cadastrados no sistema.",
		tags = {"Perfis Fiscais"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de perfis fiscais obtida com sucesso",
				content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = TaxProfileResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
		}
	)
	@GetMapping
	ResponseEntity<List<TaxProfileResponseDTO>> listTaxProfiles();
	
	@Operation(
		summary = "Excluir (desativar) um perfil fiscal",
		description = "Remove logicamente um perfil fiscal com base em seu ID. Apenas administradores podem executar esta ação.",
		tags = {"Perfis Fiscais"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Perfil fiscal excluído com sucesso"),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Perfil fiscal não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	@DeleteMapping("/{id}")
	ResponseEntity<Void> deleteTaxProfile(
		@Parameter(description = "ID do perfil fiscal") @PathVariable UUID id);
}

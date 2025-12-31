package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.cash.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Cash Register", description = "Abertura, fechamento e controle de caixa")
public interface CashRegisterControllerDocs {
	
	@Operation(
		summary = "Abertura de Caixa",
		description = "Abrir o Fluxo do Caixa.",
		responses = {
			@ApiResponse(responseCode = "201",
				description = "Abertura do caixa concluido",
				content = @Content(schema = @Schema(implementation = CashRegisterResponseDTO.class)))
		}
	)
	ResponseEntity<CashRegisterResponseDTO> openCashRegister(CashRegisterOpenRequestDTO dto);
	
	@Operation(
		summary = "Fechamento de Caixa",
		description = "Fechar o Fluxo do Caixa.",
		responses = {
			@ApiResponse(responseCode = "201",
				description = "Fechamento do caixa concluido",
				content = @Content(schema = @Schema(implementation = CashRegisterResponseDTO.class)))
		}
	)
	ResponseEntity<CashRegisterResponseDTO> closeCashRegister(UUID id, CashRegisterCloseRequestDTO dto);
	
	@Operation(
		summary = "Bucar caixa por ID",
		description = "Buscar Caixa por ID.",
		responses = {
			@ApiResponse(responseCode = "201",
				description = "Caixas encontrado por Id",
				content = @Content(schema = @Schema(implementation = CashRegisterResponseDTO.class)))
		}
	)
	ResponseEntity<CashRegisterResponseDTO> findCashRegisterById(UUID id);
}

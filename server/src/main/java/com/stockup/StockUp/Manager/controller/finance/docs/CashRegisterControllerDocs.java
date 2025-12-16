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
		summary = "Abrir caixa",
		description = "Realiza a abertura de um caixa.",
		responses = @ApiResponse(responseCode = "201")
	)
	ResponseEntity<CashRegisterResponseDTO> open(CashRegisterOpenRequestDTO dto);
	
	@Operation(summary = "Fechar caixa")
	ResponseEntity<CashRegisterResponseDTO> close(UUID id, CashRegisterCloseRequestDTO dto);
	
	@Operation(summary = "Buscar caixa por ID")
	ResponseEntity<CashRegisterResponseDTO> findById(UUID id);
}

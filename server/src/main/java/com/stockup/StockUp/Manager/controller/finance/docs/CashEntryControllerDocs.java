package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Cash Entry", description = "Lançamentos de movimentação de caixa")
public interface CashEntryControllerDocs {
	
	@Operation(
		summary = "Registrar entrada/saída",
		description = "Registra uma nova movimentação no caixa (entrada ou saída).",
		responses = {
			@ApiResponse(responseCode = "201",
				description = "Lançamento criado",
				content = @Content(schema = @Schema(implementation = CashEntryResponseDTO.class)))
		}
	)
	ResponseEntity<CashEntryResponseDTO> create(CashEntryRequestDTO dto);
	
	@Operation(summary = "Buscar por ID")
	ResponseEntity<CashEntryResponseDTO> findById(UUID id);
	
	@Operation(summary = "Listar todos os lançamentos do caixa")
	ResponseEntity<Page<CashEntryResponseDTO>> listByCashRegister(Pageable pageable);
}

package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Cash Movements", description = "Movimentações de Caixa")
public interface CashMovementControllerDocs {
	
	@Operation(summary = "Registrar movimentação", description = "Cria uma nova movimentação no caixa.")
	ResponseEntity<CashMovementResponseDTO> create(CashMovementRequestDTO dto);
	
	@Operation(summary = "Buscar por ID")
	ResponseEntity<CashMovementResponseDTO> findById(UUID id);
	
	@Operation(summary = "Listar movimentações da sessão")
	ResponseEntity<List<CashMovementResponseDTO>> findBySession(UUID sessionId);
	
	@Operation(summary = "Excluir movimentação")
	ResponseEntity<Void> delete(UUID id);
}

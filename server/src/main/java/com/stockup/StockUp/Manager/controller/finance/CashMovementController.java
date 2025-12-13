package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.CashMovementControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashMovementResponseDTO;
import com.stockup.StockUp.Manager.service.finance.ICashMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/cash/movements")
@RequiredArgsConstructor
public class CashMovementController implements CashMovementControllerDocs {
	
	private final ICashMovementService service;
	
	@Override
	@PostMapping
	public ResponseEntity<CashMovementResponseDTO> create(@RequestBody @Valid CashMovementRequestDTO dto) {
		try {
			CashMovementResponseDTO response = service.create(dto);
			AuditLogger.log("CASH_MOVEMENT", getCurrentUser(), "CREATE", response.id().toString());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			AuditLogger.log("CREATE CASH_MOVEMENT", getCurrentUser() ,"FAILED", "Error creating cash movement: " + e.getMessage());
			throw new RuntimeException( "Error creating CASH_MOVEMENT", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CashMovementResponseDTO> findById(@PathVariable UUID id) {
		CashMovementResponseDTO response = service.findById(id);
		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping("/session/{sessionId}")
	public ResponseEntity<List<CashMovementResponseDTO>> findBySession(@PathVariable UUID sessionId) {
		List<CashMovementResponseDTO> list = service.findBySession(sessionId);
		return ResponseEntity.ok(list);
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		AuditLogger.log("CASH_MOVEMENT", getCurrentUser(), "DELETE", id.toString());
		return ResponseEntity.noContent().build();
	}
}

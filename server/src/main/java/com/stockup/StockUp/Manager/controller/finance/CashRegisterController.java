package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.CashRegisterControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.cash.*;
import com.stockup.StockUp.Manager.service.finance.ICashRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/cash/registers")
@RequiredArgsConstructor
public class CashRegisterController implements CashRegisterControllerDocs {
	
	private final ICashRegisterService service;
	
	@Override
	@PostMapping("/open")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CashRegisterResponseDTO> open(@RequestBody @Valid CashRegisterOpenRequestDTO dto) {
		try {
			AuditLogger.log("OPEN CASH_REGISTER", getCurrentUser() ,"SUCCESS", "Open with successfully");
			return ResponseEntity.ok(service.openRegister(dto));
		} catch (Exception e) {
			AuditLogger.log("OPEN CASH_REGISTER", getCurrentUser() ,"FAILED", "Error open cash register : " + e.getMessage());
			throw new RuntimeException( "Error open CASH_REGISTER", e);
		}
	}
	
	@Override
	@PostMapping("/{id}/close")
	public ResponseEntity<CashRegisterResponseDTO> close(
		@PathVariable UUID id,
		@RequestBody @Valid CashRegisterCloseRequestDTO dto
	) {
		try {
			AuditLogger.log("CLOSE CASH_REGISTER", getCurrentUser() ,"SUCCESS", "Close with successfully");
			return ResponseEntity.ok(service.closeRegister(id, dto));
		} catch (Exception e) {
			AuditLogger.log("CLOSE CASH_REGISTER", getCurrentUser() ,"FAILED", "Error close cash register : " + e.getMessage());
			throw new RuntimeException( "Error close CASH_REGISTER", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CashRegisterResponseDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findById(id));
	}
}

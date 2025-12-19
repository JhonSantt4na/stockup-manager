package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.CashEntryControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.mapper.finance.CashEntryMapper;
import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import com.stockup.StockUp.Manager.service.finance.ICashEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/cash/entries")
@RequiredArgsConstructor
public class CashEntryController implements CashEntryControllerDocs {
	
	private final ICashEntryService service;
	private final CashEntryMapper mapper;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<CashEntryResponseDTO> createCashEntry(CashEntryRequestDTO dto) {
		try {
			AuditLogger.log("CREATE CASH_ENTRY", getCurrentUser() ,"SUCCESS", "Created with successfully");
			return ResponseEntity.ok(service.createCashEntry(dto));
		} catch (Exception e) {
			AuditLogger.log("CREATE CASH_ENTRY", getCurrentUser() ,"FAILED", "Error creating cash_entry: " + e.getMessage());
			throw new RuntimeException( "Error creating CASH_ENTRY", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CashEntryResponseDTO> findById(@PathVariable UUID id) {
		AuditLogger.log("FIND CASH_ENTRY", getCurrentUser() ,"SUCCESS", "Find Cash_Entry successfully with id = " + id);
		return ResponseEntity.ok(service.findCashEntryById(id));
	}
	
	public ResponseEntity<Page<CashEntryResponseDTO>> listByCashRegister(Pageable pageable) {
		AuditLogger.log("Listing All CASH_ENTRY", getCurrentUser() ,"SUCCESS", "List all Cash_Entry successfully");
		Page<CashEntry> entries = service.findAllCashEntry(pageable);
		if (entries.isEmpty()) {
			return null;
		}
		return ResponseEntity.ok(entries.map(mapper::toResponse));
	}
}

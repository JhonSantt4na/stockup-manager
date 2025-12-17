package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.PayableControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payable.PayableResponseDTO;
import com.stockup.StockUp.Manager.service.finance.IPayableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/payables")
@RequiredArgsConstructor
public class PayableController implements PayableControllerDocs {
	
	private final IPayableService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PayableResponseDTO> create(@RequestBody @Valid PayableRequestDTO dto) {
		try {
			AuditLogger.log("CREATE PAYABLE", getCurrentUser(), "CREATE", dto.toString());
			return ResponseEntity.ok(service.create(dto));
		} catch (Exception e) {
			AuditLogger.log("CREATE PAYABLE", getCurrentUser() ,"FAILED", "Error creating payable: " + e.getMessage());
			throw new RuntimeException("Error creating PAYABLE",e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<PayableResponseDTO> findById(@PathVariable UUID id) {
		AuditLogger.log("FIND PAYABLE WITH ID", getCurrentUser(), "FINDING", id.toString());
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<PayableResponseDTO>> findAll() {
		// Implements
		return ResponseEntity.ok(List.of());
	}
	
	@Override
	public ResponseEntity<List<PayableResponseDTO>> findBySupplier(UUID supplierId) {
		// Implements
		return ResponseEntity.ok(List.of());
	}

//	@Override
//	@GetMapping("/supplier/{supplierId}")
//	public List<PayableResponseDTO> findBySupplier(@PathVariable UUID supplierId) {
//		return service.findBySupplier(supplierId);
//	}
}

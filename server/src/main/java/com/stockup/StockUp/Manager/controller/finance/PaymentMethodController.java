package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.PaymentMethodControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import com.stockup.StockUp.Manager.service.finance.IPaymentMethodService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController implements PaymentMethodControllerDocs {
	
	private final IPaymentMethodService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PaymentMethodResponseDTO> createPaymentMethod(@RequestBody @Valid PaymentMethodRequestDTO dto) {
		try {
			AuditLogger.log("CREATE PAYMENT_METHOD", getCurrentUser(), "CREATE", dto.toString());
			return ResponseEntity.ok(service.createPaymentMethod(dto));
		} catch (Exception e) {
			AuditLogger.log("CREATE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error creating Payment Method: " + e.getMessage());
			throw new RuntimeException("Error creating PAYMENT_METHOD",e);
		}
	}
	
	@Override
	public ResponseEntity<List<PaymentMethodResponseDTO>> findAllPaymentMethod() {
		// Implements
		return ResponseEntity.ok(List.of());
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<PaymentMethodResponseDTO> findPaymentMethodById(@PathVariable @NotNull UUID id) {
		AuditLogger.log("FIND PAYMENT METHOD WITH ID", getCurrentUser(), "FINDING", id.toString());
		return ResponseEntity.ok(service.findPaymentMethodById(id));
	}
	
	@Override
	@PutMapping("/{id}")
	public ResponseEntity<PaymentMethodResponseDTO> updatePaymentMethod(
		@PathVariable @NotNull UUID id,
		@RequestBody @Valid PaymentMethodRequestDTO dto
	) {
		try {
			AuditLogger.log("UPDATE PAYMENT_METHOD", getCurrentUser(), "UPDATED", dto.toString());
			return ResponseEntity.ok(service.updatePaymentMethod(id, dto));
		} catch (Exception e) {
			AuditLogger.log("UPDATE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error updating Payment Method: " + e.getMessage());
			throw new RuntimeException("Error updating PAYMENT_METHOD",e);
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePaymentMethod(@PathVariable @NotNull UUID id) {
		try {
			AuditLogger.log("REMOVE PAYMENT_METHOD WITH ID", getCurrentUser(), "REMOVE", id.toString());
			service.deletePaymentMethod(id);
		} catch (Exception e) {
			AuditLogger.log("REMOVE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error removing Payment Method: " + e.getMessage());
			throw new RuntimeException("Error removing PAYMENT_METHOD",e);
		}
	}
	
}

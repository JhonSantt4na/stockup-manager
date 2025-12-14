package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.PaymentMethodControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import com.stockup.StockUp.Manager.service.finance.IPaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
	public PaymentMethodResponseDTO create(@RequestBody @Valid PaymentMethodRequestDTO dto) {
		try {
			AuditLogger.log("CREATE PAYMENT_METHOD", getCurrentUser(), "CREATE", dto.toString());
			return service.create(dto);
		} catch (Exception e) {
			AuditLogger.log("CREATE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error creating Payment Method: " + e.getMessage());
			throw new RuntimeException("Error creating PAYMENT_METHOD",e);
		}
	}
	
	@Override
	public List<PaymentMethodResponseDTO> findAll() {
		// Implements
		return List.of();
	}
	
	@Override
	@GetMapping("/{id}")
	public PaymentMethodResponseDTO findById(@PathVariable UUID id) {
		AuditLogger.log("FIND PAYMENT METHOD WITH ID", getCurrentUser(), "FINDING", id.toString());
		return service.findById(id);
	}
	
	@Override
	@PutMapping("/{id}")
	public PaymentMethodResponseDTO update(
		@PathVariable UUID id,
		@RequestBody @Valid PaymentMethodRequestDTO dto
	) {
		try {
			AuditLogger.log("UPDATE PAYMENT_METHOD", getCurrentUser(), "UPDATED", dto.toString());
			return service.update(id, dto);
		} catch (Exception e) {
			AuditLogger.log("UPDATE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error updating Payment Method: " + e.getMessage());
			throw new RuntimeException("Error updating PAYMENT_METHOD",e);
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		try {
			AuditLogger.log("REMOVE PAYMENT_METHOD WITH ID", getCurrentUser(), "REMOVE", id.toString());
			service.delete(id);
		} catch (Exception e) {
			AuditLogger.log("REMOVE PAYMENT_METHOD", getCurrentUser() ,"FAILED", "Error removing Payment Method: " + e.getMessage());
			throw new RuntimeException("Error removing PAYMENT_METHOD",e);
		}
	}
}

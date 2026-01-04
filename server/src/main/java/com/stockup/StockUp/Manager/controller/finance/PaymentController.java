package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.PaymentControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import com.stockup.StockUp.Manager.service.finance.IPaymentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {
	
	private final IPaymentService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody @Valid PaymentRequestDTO dto) {
		try {
			AuditLogger.log("CREATE PAYMENT", getCurrentUser(), "CREATE", dto.toString());
			return ResponseEntity.ok(service.createPayment(dto));
		} catch (Exception e) {
			AuditLogger.log("CREATE PAYMENT", getCurrentUser() ,"FAILED", "Error creating payment: " + e.getMessage());
			throw new RuntimeException("Error creating PAYMENT",e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<PaymentResponseDTO> findPaymentById(@PathVariable @NotNull UUID id) {
		AuditLogger.log("FIND PAYABLE WITH ID", getCurrentUser(), "FINDING", id.toString());
		return ResponseEntity.ok(service.findPaymentById(id));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<PaymentResponseDTO>> findAllPayment(@PageableDefault(size = 10) Pageable pageable) {
		Page<PaymentResponseDTO> payments = service.findAllPayment(pageable);
		return ResponseEntity.ok(payments.stream().toList());
	}
	
	@Override
	@GetMapping("/reference/{referenceId}")
	public ResponseEntity<List<PaymentResponseDTO>> findPaymentByReference(
		@PathVariable UUID referenceId) {
		List<PaymentResponseDTO> payments = Collections.singletonList(service.findPaymentById(referenceId));
        return ResponseEntity.ok(payments);
	}
	
}


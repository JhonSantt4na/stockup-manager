package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.finance.docs.PaymentControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import com.stockup.StockUp.Manager.service.finance.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
	public PaymentResponseDTO create(@RequestBody @Valid PaymentRequestDTO dto) {
		try {
			AuditLogger.log("CREATE PAYMENT", getCurrentUser(), "CREATE", dto.toString());
			return service.create(dto);
		} catch (Exception e) {
			AuditLogger.log("CREATE PAYMENT", getCurrentUser() ,"FAILED", "Error creating payment: " + e.getMessage());
			throw new RuntimeException("Error creating PAYMENT",e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public PaymentResponseDTO findById(@PathVariable UUID id) {
		AuditLogger.log("FIND PAYABLE WITH ID", getCurrentUser(), "FINDING", id.toString());
		return service.findById(id);
	}
	
	@Override
	@GetMapping
	public List<PaymentResponseDTO> findAll() {
		// Implements
		return List.of();
	}
	
	@Override
	public List<PaymentResponseDTO> findByReference(UUID referenceId) {
		// Implements
		return List.of();
	}
	
}


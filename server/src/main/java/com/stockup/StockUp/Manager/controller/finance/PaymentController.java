package com.stockup.StockUp.Manager.controller.finance;

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

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {
	
	private final IPaymentService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentResponseDTO create(@RequestBody @Valid PaymentRequestDTO dto) {
		return service.create(dto);
	}
	
	@Override
	@GetMapping("/{id}")
	public PaymentResponseDTO findById(@PathVariable UUID id) {
		return service.findById(id);
	}
	
	@Override
	@GetMapping
	public List<PaymentResponseDTO> findAll() {
		return List.of();
	}
	
	@Override
	public List<PaymentResponseDTO> findByReference(UUID referenceId) {
		return List.of();
	}
	
}


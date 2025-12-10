package com.stockup.StockUp.Manager.controller.finance;

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

@RestController
@RequestMapping("/api/v1/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController implements PaymentMethodControllerDocs {
	
	private final IPaymentMethodService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PaymentMethodResponseDTO create(@RequestBody @Valid PaymentMethodRequestDTO dto) {
		return service.create(dto);
	}
	
	@Override
	public List<PaymentMethodResponseDTO> findAll() {
		return List.of();
	}
	
	@Override
	@GetMapping("/{id}")
	public PaymentMethodResponseDTO findById(@PathVariable UUID id) {
		return service.findById(id);
	}
	
	@Override
	@PutMapping("/{id}")
	public PaymentMethodResponseDTO update(
		@PathVariable UUID id,
		@RequestBody @Valid PaymentMethodRequestDTO dto
	) {
		return service.update(id, dto);
	}
	
	@Override
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
}

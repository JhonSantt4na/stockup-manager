package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.controller.sales.docs.OrderControllerDocs;
import com.stockup.StockUp.Manager.dto.sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.order.OrderResponseDTO;
import com.stockup.StockUp.Manager.service.sales.impl.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController implements OrderControllerDocs {
	
	private final OrderService service;
	
	@Override
	@PostMapping
	public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Override
	@GetMapping("/number/{orderNumber}")
	public ResponseEntity<OrderResponseDTO> findByNumber(@PathVariable String orderNumber) {
		return ResponseEntity.ok(service.findByOrderNumber(orderNumber));
	}
	
	@Override
	@GetMapping
	public ResponseEntity<Page<OrderResponseDTO>> findAll(Pageable pageable) {
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@Override
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponseDTO> updateStatus(
		@PathVariable UUID id,
		@RequestParam OrderStatus status) {
		return ResponseEntity.ok(service.updateStatus(id, status));
	}
	
	@Override
	@PatchMapping("/{id}/cancel")
	public ResponseEntity<OrderResponseDTO> cancel(@PathVariable UUID id) {
		return ResponseEntity.ok(service.cancel(id));
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
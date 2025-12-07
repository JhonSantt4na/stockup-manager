package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.OrderControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderResponseDTO;
import com.stockup.StockUp.Manager.service.sales.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderControllerDocs {
	
	private final IOrderService service;
	
	@Override
	@PostMapping("/create")
	public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
		try {
			AuditLogger.log("ORDER_CREATE", dto.customerId().toString(), "ATTEMPT", "Creating new order");
			OrderResponseDTO created = service.create(dto);
			AuditLogger.log("ORDER_CREATE", created.id().toString(), "SUCCESS", "Order created successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_CREATE", dto.customerId().toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable UUID id) {
		try {
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "ATTEMPT", "Fetching order by ID");
			OrderResponseDTO response = service.findById(id);
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "SUCCESS", "Order found");
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@GetMapping("/number/{orderNumber}")
	public ResponseEntity<OrderResponseDTO> findOrderByNumber(@PathVariable String orderNumber) {
		try {
			AuditLogger.log("ORDER_FIND_BY_NUMBER", orderNumber, "ATTEMPT", "Fetching order by order number");
			OrderResponseDTO response = service.findByOrderNumber(orderNumber);
			AuditLogger.log("ORDER_FIND_BY_NUMBER", orderNumber, "SUCCESS", "Order found");
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_FIND_BY_NUMBER", orderNumber, "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@GetMapping
	public ResponseEntity<Page<OrderResponseDTO>> findAllOrder(Pageable pageable) {
		AuditLogger.log("ORDER_LIST", "system", "ATTEMPT", "Listing all orders");
		Page<OrderResponseDTO> response = service.findAll(pageable);
		AuditLogger.log("ORDER_LIST", "system", "SUCCESS", "Orders listed successfully");
		return ResponseEntity.ok(response);
	}
	
	@Override
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponseDTO> updateStatusOrder(
		@PathVariable UUID id,
		@RequestParam OrderStatus status
	) {
		try {
			AuditLogger.log("ORDER_UPDATE_STATUS", id.toString(), "ATTEMPT",
				"Updating status to: " + status);
			OrderResponseDTO updated = service.updateStatus(id, status);
			AuditLogger.log("ORDER_UPDATE_STATUS", id.toString(), "SUCCESS",
				"Status updated successfully");
			return ResponseEntity.ok(updated);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_UPDATE_STATUS", id.toString(), "FAILED",
				"Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@PatchMapping("/{id}/cancel")
	public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable UUID id) {
		try {
			AuditLogger.log("ORDER_CANCEL", id.toString(), "ATTEMPT", "Cancelling order");
			OrderResponseDTO canceled = service.cancel(id);
			AuditLogger.log("ORDER_CANCEL", id.toString(), "SUCCESS", "Order cancelled successfully");
			return ResponseEntity.ok(canceled);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_CANCEL", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
		try {
			AuditLogger.log("ORDER_DELETE", id.toString(), "ATTEMPT", "Deleting order permanently");
			service.delete(id);
			AuditLogger.log("ORDER_DELETE", id.toString(), "SUCCESS", "Order deleted");
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_DELETE", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
}

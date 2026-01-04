package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.OrderControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderResponseDTO;
import com.stockup.StockUp.Manager.service.sales.IOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	@PostMapping("/createCashMovement")
	public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
		try {
			AuditLogger.log("ORDER_CREATE", dto.customerId().toString(), "ATTEMPT", "Creating new order");
			OrderResponseDTO created = service.createOrder(dto);
			AuditLogger.log("ORDER_CREATE", created.id().toString(), "SUCCESS", "Order created successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_CREATE", dto.customerId().toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable @NotNull UUID id) {
		try {
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "ATTEMPT", "Fetching order by ID");
			OrderResponseDTO response = service.findOrderById(id);
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "SUCCESS", "Order found");
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_FIND_BY_ID", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@GetMapping("/number/{orderNumber}")
	public ResponseEntity<OrderResponseDTO> findOrderByNumber(@PathVariable @NotNull String orderNumber) {
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
	public ResponseEntity<Page<OrderResponseDTO>> findAllOrder(@PageableDefault(size = 10) Pageable pageable) {
		AuditLogger.log("ORDER_LIST", "system", "ATTEMPT", "Listing all orders");
		Page<OrderResponseDTO> response = service.findAllOrder(pageable);
		AuditLogger.log("ORDER_LIST", "system", "SUCCESS", "Orders listed successfully");
		return ResponseEntity.ok(response);
	}
	
	@Override
	@PatchMapping("/{id}/status")
	public ResponseEntity<OrderResponseDTO> updateStatusOrder(
		@PathVariable @NotNull UUID id,
		@RequestParam @Valid OrderStatus status
	) {
		try {
			AuditLogger.log("ORDER_UPDATE_STATUS", id.toString(), "ATTEMPT",
				"Updating status to: " + status);
			OrderResponseDTO updated = service.updateOrderStatus(id, status);
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
	@PatchMapping("/{id}/cancelOrder")
	public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable @NotNull UUID id) {
		try {
			AuditLogger.log("ORDER_CANCEL", id.toString(), "ATTEMPT", "Cancelling order");
			OrderResponseDTO canceled = service.cancelOrder(id);
			AuditLogger.log("ORDER_CANCEL", id.toString(), "SUCCESS", "Order cancelled successfully");
			return ResponseEntity.ok(canceled);
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_CANCEL", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
	
	@Override
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable @NotNull UUID id) {
		try {
			AuditLogger.log("ORDER_DELETE", id.toString(), "ATTEMPT", "Deleting order permanently");
			service.deleteOrder(id);
			AuditLogger.log("ORDER_DELETE", id.toString(), "SUCCESS", "Order deleted");
			return ResponseEntity.noContent().build();
			
		} catch (Exception e) {
			AuditLogger.log("ORDER_DELETE", id.toString(), "FAILED", "Error: " + e.getMessage());
			throw e;
		}
	}
}

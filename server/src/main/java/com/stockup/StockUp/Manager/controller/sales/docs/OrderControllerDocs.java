package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Orders", description = "Operations related to sales orders.")
public interface OrderControllerDocs {
	
	@Operation(
		summary = "Create a new order",
		description = "Creates a new order containing one or more items.",
		responses = {
			@ApiResponse(
				responseCode = "201",
				description = "Order successfully created.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "400", description = "Invalid request payload."),
			@ApiResponse(responseCode = "404", description = "Customer or product not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> createOrder(OrderRequestDTO dto);
	
	@Operation(
		summary = "Find order by ID",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Order found.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> findOrderById(UUID id);
	
	@Operation(
		summary = "Find order by order number",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Order found.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> findOrderByNumber(String orderNumber);
	
	@Operation(
		summary = "List all orders",
		description = "Returns a pageable list of orders.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "List returned.",
				content = @Content(schema = @Schema(implementation = Page.class))
			)
		}
	)
	ResponseEntity<Page<OrderResponseDTO>> findAllOrder(Pageable pageable);
	
	@Operation(
		summary = "Update order status",
		description = "Updates the order status (ex: PENDING → CONFIRMED).",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Order updated.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> updateStatusOrder(UUID id, OrderStatus status);
	
	@Operation(
		summary = "Cancel order",
		description = "Marks the order IPurchaseOrderService canceled.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Order canceled.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> cancelOrder(UUID id);
	
	@Operation(
		summary = "Delete order",
		description = "Soft delete: marks the order IPurchaseOrderService disabled or deleted.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Order deleted."),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<Void> deleteOrder(UUID id);
}
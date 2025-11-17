package com.stockup.StockUp.Manager.controller.sales.docs;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.order.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.List;
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
	ResponseEntity<OrderResponseDTO> create(OrderRequestDTO dto);
	
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
	ResponseEntity<OrderResponseDTO> findById(UUID id);
	
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
	ResponseEntity<OrderResponseDTO> findByNumber(String orderNumber);
	
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
	ResponseEntity<Page<OrderResponseDTO>> findAll(Pageable pageable);
	
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
	ResponseEntity<OrderResponseDTO> updateStatus(UUID id, OrderStatus status);
	
	@Operation(
		summary = "Cancel order",
		description = "Marks the order as canceled.",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Order canceled.",
				content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))
			),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<OrderResponseDTO> cancel(UUID id);
	
	@Operation(
		summary = "Delete order",
		description = "Soft delete: marks the order as disabled or deleted.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Order deleted."),
			@ApiResponse(responseCode = "404", description = "Order not found.")
		}
	)
	ResponseEntity<Void> delete(UUID id);
}
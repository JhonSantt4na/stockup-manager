package com.stockup.StockUp.Manager.dto.sales.order;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderUpdateDTO(
	
	OrderStatus status,
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime confirmedAt,
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime paidAt,
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	LocalDateTime cancelledAt,
	
	@PositiveOrZero(message = "discountTotal must be zero or positive.")
	BigDecimal discountTotal,
	
	@PositiveOrZero(message = "shippingTotal must be zero or positive.")
	BigDecimal shippingTotal,
	
	@PositiveOrZero(message = "taxTotal must be zero or positive.")
	BigDecimal taxTotal

) {}

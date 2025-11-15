package com.stockup.StockUp.Manager.model.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
	
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
		timezone = "GMT"
	)
	private LocalDateTime moment;
	private LocalDateTime cancelledAt;
	private LocalDateTime confirmedAt;
	private LocalDateTime paidAt;
	
	@Column(nullable = false, unique = true, length = 30)
	private String orderNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private OrderStatus orderStatus;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderItem> items = new LinkedHashSet<>();
	
	private BigDecimal subtotal;
	private BigDecimal discountTotal;
	private BigDecimal shippingTotal;
	private BigDecimal taxTotal;
	private BigDecimal total;
	private BigDecimal totalPaid;
	private BigDecimal totalRefunded;
	
	public BigDecimal calculateItemsTotal() {
		return items.stream()
			.map(OrderItem::getSubTotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}

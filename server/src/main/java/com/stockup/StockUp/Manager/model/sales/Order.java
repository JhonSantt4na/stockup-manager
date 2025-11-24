package com.stockup.StockUp.Manager.model.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.customer.Customer;
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
	
	@Column(nullable = false)
	private LocalDateTime moment;
	private LocalDateTime cancelledAt;
	private LocalDateTime confirmedAt;
	private LocalDateTime paidAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@Column(nullable = false, unique = true, length = 30)
	private String orderNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private OrderStatus orderStatus;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderItem> items = new LinkedHashSet<>();
	
	@Column(precision = 19, scale = 2)
	private BigDecimal subtotal;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal discountTotal;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal shippingTotal;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal taxTotal;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal total;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal totalPaid;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal totalRefunded;
	
	public BigDecimal calculateItemsTotal() {
		return items.stream()
			.map(OrderItem::getSubTotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}

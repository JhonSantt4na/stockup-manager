package com.stockup.StockUp.Manager.model.sales;

import com.stockup.StockUp.Manager.model.PK.OrderItemPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
	
	@EmbeddedId
	private OrderItemPK id = new OrderItemPK();
	
	@Column(nullable = false)
	private Integer quantity;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal unitPrice;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal discount;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal finalPrice;
	
	private String productName;
	private String productSku;
	private String productImage;
	
	@MapsId("order")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@MapsId("product")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	public BigDecimal getSubTotal() {
		if (finalPrice == null || quantity == null) {
			return BigDecimal.ZERO;
		}
		return finalPrice.multiply(BigDecimal.valueOf(quantity));
	}
}

package com.stockup.StockUp.Manager.model.PK;

import com.stockup.StockUp.Manager.model.sales.Order;
import com.stockup.StockUp.Manager.model.sales.Product;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class OrderItemPK implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	public OrderItemPK() {}
	
	public OrderItemPK(Order order, Product product) {
		this.order = order;
		this.product = product;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(order.getId(), product.getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		OrderItemPK other = (OrderItemPK) obj;
		return Objects.equals(order.getId(), other.order.getId()) &&
			Objects.equals(product.getId(), other.product.getId());
	}
}
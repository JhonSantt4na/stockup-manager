package com.stockup.StockUp.Manager.model.procurement;

import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.catalog.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItem extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id", nullable = false)
	@NotNull(message = "O pedido de compra é obrigatório.")
	private PurchaseOrder purchaseOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@NotNull(message = "O produto é obrigatório.")
	private Product product;
	
	@Column(nullable = false)
	@NotNull(message = "A quantidade é obrigatória.")
	private Integer quantity;
	
	@Column(nullable = false, precision = 19, scale = 4)
	@NotNull(message = "O preço de custo é obrigatório.")
	private BigDecimal costPrice;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal subtotal;
	
	@PrePersist
	@PreUpdate
	public void calculateSubtotal() {
		if (quantity != null && costPrice != null) {
			this.subtotal = costPrice.multiply(BigDecimal.valueOf(quantity));
		}
	}
}

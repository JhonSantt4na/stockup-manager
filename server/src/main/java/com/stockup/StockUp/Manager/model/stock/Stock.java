package com.stockup.StockUp.Manager.model.stock;

import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.catalog.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"product_id", "warehouse_id"})
	})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@NotNull(message = "O produto é obrigatório.")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	@NotNull(message = "O local é obrigatório.")
	private Warehouse warehouse;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal quantity = BigDecimal.ZERO;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal minimumQuantity = BigDecimal.ZERO;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal maximumQuantity;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal reservedQuantity = BigDecimal.ZERO;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal lastCostPrice;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal averageCost;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PreUpdate
	@PrePersist
	public void prePersist() {
		this.updatedAt = LocalDateTime.now();
	}
}

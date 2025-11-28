package com.stockup.StockUp.Manager.model.stock;

import com.stockup.StockUp.Manager.Enums.Stock.MovementReason;
import com.stockup.StockUp.Manager.Enums.Stock.MovementType;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.catalog.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	@NotNull
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	@NotNull
	private Warehouse warehouse;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MovementType movementType;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MovementReason reason;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal quantity;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal previousQuantity;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal finalQuantity;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal unitCost;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal totalCost;
	
	@Column(length = 36)
	private String relatedDocumentId;
	
	@Column(length = 36)
	private String operatorId;
	
	@Column(nullable = false)
	private LocalDateTime timestamp;
	
	@PrePersist
	public void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
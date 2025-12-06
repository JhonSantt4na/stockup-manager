package com.stockup.StockUp.Manager.model.procurement;

import com.stockup.StockUp.Manager.Enums.pocurement.PurchaseOrderStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	@NotNull(message = "O fornecedor é obrigatório.")
	private Supplier supplier;
	
	@Column(nullable = false, unique = true)
	@NotNull(message = "O número do pedido é obrigatório.")
	private String orderNumber;
	
	private LocalDate expectedArrivalDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PurchaseOrderStatus status = PurchaseOrderStatus.PENDING;
	
	@OneToMany(
		mappedBy = "purchaseOrder",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<PurchaseItem> items;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal total = BigDecimal.ZERO;
}

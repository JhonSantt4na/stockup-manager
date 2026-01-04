package com.stockup.StockUp.Manager.model.finance.payable;

import com.stockup.StockUp.Manager.Enums.finance.PayableStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.finance.payments.Payment;
import com.stockup.StockUp.Manager.model.procurement.Supplier;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payable extends BaseEntity {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	@Column(nullable = false)
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PayableStatus status;
	
	@Column(nullable = false)
	private Integer installmentNumber;
	
	@Column(nullable = false)
	private Integer installmentTotal;
	
	@Column(nullable = false)
	private String provider;
	
}

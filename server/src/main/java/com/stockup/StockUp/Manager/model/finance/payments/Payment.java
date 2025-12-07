package com.stockup.StockUp.Manager.model.finance.payments;

import com.stockup.StockUp.Manager.Enums.finance.PaymentStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.sales.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
	
	@Column(nullable = false, unique = true, length = 50)
	private String code;
	
	@Column(nullable = false)
	private UUID referenceId;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_method_id", nullable = false)
	private PaymentMethod method;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;
	
	@Column(length = 255)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	private LocalDateTime paidAt;
	
	@OneToOne(mappedBy = "payment", cascade = CascadeType.ALL)
	private PaymentGatewayTransaction gatewayTransaction;
}

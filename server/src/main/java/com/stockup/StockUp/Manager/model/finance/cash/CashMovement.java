package com.stockup.StockUp.Manager.model.finance.cash;

import com.stockup.StockUp.Manager.Enums.finance.MovementTypePayment;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashMovement extends BaseEntity {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "session_id")
	private CashRegisterSession session;
	
	@Column(nullable = false)
	private BigDecimal amount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MovementTypePayment type;
	
	@Column(length = 255)
	private String description;
	
	@Column(nullable = false)
	private LocalDateTime timestamp;
}

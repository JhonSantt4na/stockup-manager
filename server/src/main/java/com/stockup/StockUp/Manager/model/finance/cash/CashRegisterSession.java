package com.stockup.StockUp.Manager.model.finance.cash;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cash_register_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashRegisterSession extends BaseEntity {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "cash_register_id")
	private CashRegister cashRegister;
	
	@Column(nullable = false)
	private UUID openedByUserId;
	
	private UUID closedByUserId;
	
	@Column(nullable = false)
	private LocalDateTime openedAt;
	
	private LocalDateTime closedAt;
	
	@Column(nullable = false)
	private BigDecimal openingAmount;
	
	private BigDecimal closingAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SessionStatus status;
	
	public enum SessionStatus {
		OPEN,
		CLOSED,
		SUSPENDED
	}
}
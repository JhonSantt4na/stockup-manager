package com.stockup.StockUp.Manager.model.finance.cash;

import com.stockup.StockUp.Manager.Enums.finance.CashMovementType;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashEntry extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cash_register_id", nullable = false)
	private CashRegister cashRegister;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	private CashMovementType movementType;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;
	
	@Column(length = 255)
	private String description;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}

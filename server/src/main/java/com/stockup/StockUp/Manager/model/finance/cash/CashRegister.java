package com.stockup.StockUp.Manager.model.finance.cash;

import com.stockup.StockUp.Manager.Enums.finance.CashStatus;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cash_register")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashRegister extends BaseEntity {
	
	@Column(nullable = false)
	private LocalDateTime openedAt;
	
	private LocalDateTime closedAt;
	
	@Column(nullable = false, unique = true, length = 50)
	private String identifier;
	
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal openingAmount;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal closingAmount;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal systemExpectedAmount;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal differenceAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private CashStatus status;
	
	@Column(nullable = false)
	private UUID operatorOpenId;
	
	private UUID operatorCloseId;
	
	@OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CashEntry> entries = new ArrayList<>();
}
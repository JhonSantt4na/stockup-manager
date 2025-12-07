package com.stockup.StockUp.Manager.model.finance.payments;

import com.stockup.StockUp.Manager.Enums.finance.PaymentGatewayType;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod extends BaseEntity {
	
	@Column(nullable = false, unique = true, length = 50)
	private String code;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private PaymentGatewayType gatewayType;
	
	@Column(nullable = false)
	private Boolean active = true;
}
package com.stockup.StockUp.Manager.model.finance.payments;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payment_gateway_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentGatewayTransaction extends BaseEntity {
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", nullable = false)
	private Payment payment;
	
	@Column(nullable = false, unique = true)
	private String externalTransactionId;
	
	private String qrCode;
	private String pixCopiaECola;
	
	@Column(length = 255)
	private String cardBrand;
	
	private Integer installments;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal fees;
	
	@Column(precision = 19, scale = 2)
	private BigDecimal netAmount;
}

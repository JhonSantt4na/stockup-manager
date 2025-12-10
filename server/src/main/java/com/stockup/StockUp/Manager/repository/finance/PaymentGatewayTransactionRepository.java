package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.model.finance.payments.PaymentGatewayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentGatewayTransactionRepository extends JpaRepository<PaymentGatewayTransaction, UUID> {
	Optional<PaymentGatewayTransaction> findByPaymentId(UUID paymentId);
}
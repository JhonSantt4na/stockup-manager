package com.stockup.StockUp.Manager.repository.payments;

import com.stockup.StockUp.Manager.Enums.finance.PaymentStatus;
import com.stockup.StockUp.Manager.model.finance.payments.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
	
	List<Payment> findByOrder_Id(UUID orderId);
	
	List<Payment> findByStatus(PaymentStatus status);
}
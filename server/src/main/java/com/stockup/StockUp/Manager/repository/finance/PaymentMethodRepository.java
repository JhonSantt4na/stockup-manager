package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.model.finance.payments.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
	
	Optional<PaymentMethod> findByCode(String code);
	
	boolean existsByCode(String code);
}
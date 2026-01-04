package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.Enums.finance.PayableStatus;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface PayableRepository extends JpaRepository<Payable, UUID> {
	List<Payable> findByPaymentId(UUID paymentId);
	List<Payable> findBySupplierId(UUID supplierId);
	List<Payable> findByStatus(PayableStatus status);
}
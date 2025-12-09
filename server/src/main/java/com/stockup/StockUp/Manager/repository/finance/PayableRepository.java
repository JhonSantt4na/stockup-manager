package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.Enums.finance.PayableStatus;
import com.stockup.StockUp.Manager.model.finance.payable.Payable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PayableRepository extends JpaRepository<Payable, UUID> {
	
	List<Payable> findByPaymentId(UUID paymentId);
	
	List<Payable> findByStatus(PayableStatus status);
}
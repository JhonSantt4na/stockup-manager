package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.model.finance.cash.CashMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CashMovementRepository extends JpaRepository<CashMovement, UUID> {
	
	List<CashMovement> findBySessionId(UUID sessionId);
}
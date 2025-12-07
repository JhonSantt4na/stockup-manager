package com.stockup.StockUp.Manager.repository.payments;

import com.stockup.StockUp.Manager.model.finance.cash.CashRegisterSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.support.SessionStatus;
import java.util.List;
import java.util.UUID;

public interface CashRegisterSessionRepository extends JpaRepository<CashRegisterSession, UUID> {
	
	List<CashRegisterSession> findByCashRegisterId(UUID cashRegisterId);
	
	List<CashRegisterSession> findByOpenedByUserId(UUID userId);
	
	List<CashRegisterSession> findByStatus(SessionStatus status);
	
	boolean existsByCashRegisterIdAndStatus(UUID cashRegisterId, SessionStatus status);
}
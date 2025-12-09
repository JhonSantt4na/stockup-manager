package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.model.finance.cash.CashRegister;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CashRegisterRepository extends JpaRepository<CashRegister, UUID> {
	
	Optional<CashRegister> findByIdentifier(String identifier);
	
	boolean existsByIdentifier(String identifier);
}
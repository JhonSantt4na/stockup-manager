package com.stockup.StockUp.Manager.repository.finance;

import com.stockup.StockUp.Manager.model.finance.cash.CashEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CashEntryRepository extends JpaRepository<CashEntry, UUID> {
}
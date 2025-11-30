package com.stockup.StockUp.Manager.repository.stock;

import com.stockup.StockUp.Manager.model.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock,UUID> {
}
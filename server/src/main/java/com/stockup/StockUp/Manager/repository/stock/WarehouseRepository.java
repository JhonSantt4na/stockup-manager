package com.stockup.StockUp.Manager.repository.stock;

import com.stockup.StockUp.Manager.model.stock.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
}
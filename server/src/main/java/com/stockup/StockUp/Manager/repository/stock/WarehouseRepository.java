package com.stockup.StockUp.Manager.repository.stock;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.model.stock.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
	boolean existsByName(String name);
	Page<Warehouse> findByType(WarehouseType type, Pageable pageable);
	boolean existsById(UUID id);
}
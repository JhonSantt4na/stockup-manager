package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.catalog.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
	
	boolean existsBySku(String sku);
	
	Optional<Product> findBySku(String sku);
	
	Optional<Product> findByNameIgnoreCase(String name);
	
	Page<Product> findAllByEnabledTrue(Pageable pageable);
}


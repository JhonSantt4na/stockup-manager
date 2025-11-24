package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
	Optional<Brand> findByNameIgnoreCase(String name);
	boolean existsByNameIgnoreCase(String name);
}
package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.Enums.UnitOfMeasure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnitOfMeasureRepository extends JpaRepository<UnitOfMeasure, UUID> {
	Optional<UnitOfMeasure> findByAbbreviationIgnoreCase(String abbreviation);
	boolean existsByAbbreviationIgnoreCase(String abbreviation);
}
package com.stockup.StockUp.Manager.repository.catalog;

import com.stockup.StockUp.Manager.model.catalog.TaxProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxProfileRepository extends JpaRepository<TaxProfile, UUID> {
	
	Optional<TaxProfile> findByName(String name);
	
	boolean existsByName(String name);
	
	@Query("""
        SELECT t
        FROM TaxProfile t
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))
        """)
	java.util.List<TaxProfile> searchByName(String name);
}
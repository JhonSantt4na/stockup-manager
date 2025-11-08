package com.stockup.StockUp.Manager.repository.sales;

import com.stockup.StockUp.Manager.model.sales.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
	
	Optional<Category> findByName(String name);
	
	boolean existsByName(String name);
	
	@Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL AND c.enabled = true")
	Page<Category> findAllActive(Pageable pageable);
}
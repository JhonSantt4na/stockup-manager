package com.stockup.StockUp.Manager.repository.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.model.sales.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
	
	Optional<Order> findByOrderNumber(String orderNumber);
	
	@Query("SELECT o FROM Order o WHERE o.orderStatus = :status")
	List<Order> findAllByStatus(OrderStatus status);
	
	@Query("SELECT o FROM Order o WHERE o.enabled = true AND o.deletedAt IS NULL")
	Page<Order> findAllActive(Pageable pageable);
	
	boolean existsByOrderNumber(String number);
}
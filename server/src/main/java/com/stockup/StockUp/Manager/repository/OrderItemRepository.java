package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.sales.PK.OrderItemPK;
import com.stockup.StockUp.Manager.model.sales.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
	
	@Query("SELECT i FROM OrderItem i WHERE i.order.id = :orderId")
	List<OrderItem> findByOrderId(UUID orderId);
	
}
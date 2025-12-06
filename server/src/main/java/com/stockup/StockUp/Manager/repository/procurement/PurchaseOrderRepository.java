package com.stockup.StockUp.Manager.repository.procurement;

import com.stockup.StockUp.Manager.Enums.pocurement.PurchaseOrderStatus;
import com.stockup.StockUp.Manager.model.procurement.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
	List<PurchaseOrder> findBySupplierId(UUID supplierId);
	List<PurchaseOrder> findByStatus(PurchaseOrderStatus status);
	List<PurchaseOrder> findByExpectedArrivalDate(LocalDate date);
	boolean existsByOrderNumber(String orderNumber);
}
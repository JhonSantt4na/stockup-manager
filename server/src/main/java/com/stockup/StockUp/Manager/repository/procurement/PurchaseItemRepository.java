package com.stockup.StockUp.Manager.repository.procurement;

import com.stockup.StockUp.Manager.model.procurement.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, UUID> {
	List<PurchaseItem> findByPurchaseOrderId(UUID purchaseOrderId);
	List<PurchaseItem> findByProductId(UUID productId);
}
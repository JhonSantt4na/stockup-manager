package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IOrderService {
	OrderResponseDTO createOrder(OrderRequestDTO dto);
	OrderResponseDTO findOrderById(UUID id);
	OrderResponseDTO findByOrderNumber(String orderNumber);
	Page<OrderResponseDTO> findAllOrder(Pageable pageable);
	OrderResponseDTO updateOrderStatus(UUID id, OrderStatus newStatus);
	OrderResponseDTO cancelOrder(UUID id);
	void deleteOrder(UUID id);
}
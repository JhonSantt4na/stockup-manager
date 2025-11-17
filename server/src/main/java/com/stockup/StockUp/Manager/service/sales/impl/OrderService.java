package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.order.OrderResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	public OrderResponseDTO create(OrderRequestDTO dto) {
		logger.debug("Creating new order");
		return null;
	}
	
	public OrderResponseDTO findById(UUID id) {
		logger.debug("Finding order by ID: {}", id);
		return null;
	}
	
	public OrderResponseDTO findByOrderNumber(String orderNumber) {
		logger.debug("Finding order by orderNumber: {}", orderNumber);
		return null;
	}
	
	public Page<OrderResponseDTO> findAll(Pageable pageable) {
		logger.debug("Retrieving paginated list of orders");
		return new PageImpl<>(Collections.emptyList());
	}
	
	public OrderResponseDTO updateStatus(UUID id, OrderStatus newStatus) {
		logger.debug("Updating order status. ID: {}, newStatus: {}", id, newStatus);
		return null;
	}
	
	public OrderResponseDTO cancel(UUID id) {
		logger.debug("Cancelling order with ID: {}", id);
		return null;
	}
	
	public void delete(UUID id) {
		logger.debug("Soft deleting order with ID: {}", id);
		// soft delete vazio
	}
}


//validar cliente
//validar produtos e quantidades
//montar itens
//calcular totals
//gerar orderNumber (ex: "ORD-202501-000123")
//salvar no banco
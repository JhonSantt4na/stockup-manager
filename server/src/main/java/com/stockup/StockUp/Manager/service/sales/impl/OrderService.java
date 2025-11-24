package com.stockup.StockUp.Manager.service.sales.impl;

import com.stockup.StockUp.Manager.Enums.OrderStatus;
import com.stockup.StockUp.Manager.dto.sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.order.OrderResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.orderItem.OrderItemRequestDTO;
import com.stockup.StockUp.Manager.mapper.OrderMapper;
import com.stockup.StockUp.Manager.model.customer.Customer;
import com.stockup.StockUp.Manager.model.sales.Order;
import com.stockup.StockUp.Manager.model.sales.OrderItem;
import com.stockup.StockUp.Manager.model.catalog.Product;
import com.stockup.StockUp.Manager.repository.CustomerRepository;
import com.stockup.StockUp.Manager.repository.OrderRepository;
import com.stockup.StockUp.Manager.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;
	private final OrderMapper mapper;
	
	public OrderResponseDTO create(OrderRequestDTO dto) {
		
		logger.debug("Starting order creation");
		
		Customer customer = customerRepository.findById(dto.customerId())
			.orElseThrow(() -> new IllegalArgumentException("Customer not found: " + dto.customerId()));
		
		Order order = new Order();
		order.setCustomer(customer);
		order.setMoment(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.PAID);
		order.setOrderNumber(generateOrderNumber());
		
		BigDecimal subtotal = BigDecimal.ZERO;
		
		for (OrderItemRequestDTO itemDto : dto.items()) {
			
			Product product = productRepository.findById(itemDto.productId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + itemDto.productId()));
			
			if (itemDto.quantity() <= 0) {
				throw new IllegalArgumentException("Quantity must be greater than zero");
			}
			
			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setProduct(product);
			item.setQuantity(itemDto.quantity());
			item.setUnitPrice(product.getSalePrice());
			item.setDiscount(BigDecimal.ZERO);
			item.setFinalPrice(product.getSalePrice().multiply(BigDecimal.valueOf(itemDto.quantity())));
			
			item.setProductName(product.getName());
			item.setProductSku(product.getSku());
			
			order.getItems().add(item);
			
			subtotal = subtotal.add(item.getFinalPrice());
		}
		
		order.setSubtotal(subtotal);
		order.setDiscountTotal(BigDecimal.ZERO);
		order.setShippingTotal(BigDecimal.ZERO);
		order.setTaxTotal(BigDecimal.ZERO);
		order.setTotal(subtotal);
		order.setTotalPaid(BigDecimal.ZERO);
		order.setTotalRefunded(BigDecimal.ZERO);
		
		Order saved = orderRepository.save(order);
		
		logger.debug("Order created successfully: {}", saved.getId());
		
		return mapper.toResponseDTO(saved);
	}
	
	public OrderResponseDTO findById(UUID id) {
		logger.debug("Finding order by ID: {}", id);
		
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		
		return mapper.toResponseDTO(order);
	}
	
	public OrderResponseDTO findByOrderNumber(String orderNumber) {
		logger.debug("Finding order by number: {}", orderNumber);
		
		Order order = orderRepository.findByOrderNumber(orderNumber)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderNumber));
		
		return mapper.toResponseDTO(order);
	}
	
	public Page<OrderResponseDTO> findAll(Pageable pageable) {
		logger.debug("Listing orders with pagination");
		
		return orderRepository.findAll(pageable)
			.map(mapper::toResponseDTO);
	}
	
	public OrderResponseDTO updateStatus(UUID id, OrderStatus newStatus) {
		
		logger.debug("Updating order status. ID: {}, newStatus: {}", id, newStatus);
		
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		
		order.setOrderStatus(newStatus);
		
		switch (newStatus) {
			case CONFIRMED -> order.setConfirmedAt(LocalDateTime.now());
			case PAID -> order.setPaidAt(LocalDateTime.now());
			case CANCELLED -> order.setCancelledAt(LocalDateTime.now());
		}
		
		Order saved = orderRepository.save(order);
		return mapper.toResponseDTO(saved);
	}
	
	public OrderResponseDTO cancel(UUID id) {
		
		logger.debug("Cancelling order ID: {}", id);
		
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		
		order.setOrderStatus(OrderStatus.CANCELLED);
		order.setCancelledAt(LocalDateTime.now());
		
		Order saved = orderRepository.save(order);
		return mapper.toResponseDTO(saved);
	}
	
	public void delete(UUID id) {
		
		logger.debug("Soft deleting order ID: {}", id);
		
		Order order = orderRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
		
		order.setEnabled(false);
		order.setDeletedAt(LocalDateTime.now());
		
		orderRepository.save(order);
	}
	
	private String generateOrderNumber() {
		String yearMonth = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMM"));
		long count = orderRepository.count() + 1;
		return String.format("ORD-%s-%06d", yearMonth, count);
	}
}
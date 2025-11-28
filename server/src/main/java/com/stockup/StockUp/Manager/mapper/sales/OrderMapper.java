package com.stockup.StockUp.Manager.mapper.sales;

import com.stockup.StockUp.Manager.dto.Sales.order.OrderRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.order.OrderUpdateDTO;
import com.stockup.StockUp.Manager.mapper.user.CustomerMapper;
import com.stockup.StockUp.Manager.model.sales.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	uses = {CustomerMapper.class, OrderItemMapper.class}
)
public interface OrderMapper {
	
	@Mapping(target = "customer.id", source = "customerId")
	Order toEntity(OrderRequestDTO dto);
	
	OrderResponseDTO toResponseDTO(Order order);
	
	List<OrderResponseDTO> toResponseList(List<Order> orders);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "customer", ignore = true)
	void updateEntity(@MappingTarget Order order, OrderUpdateDTO dto);
}

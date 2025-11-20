package com.stockup.StockUp.Manager.mapper.sales;

import com.stockup.StockUp.Manager.dto.sales.orderItem.OrderItemRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.orderItem.OrderItemResponseDTO;
import com.stockup.StockUp.Manager.model.sales.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface OrderItemMapper {
	
	OrderItem toEntity(OrderItemRequestDTO dto);
	
	OrderItemResponseDTO toResponseDTO(OrderItem item);
	
	List<OrderItemResponseDTO> toResponseList(Set<OrderItem> items);
}

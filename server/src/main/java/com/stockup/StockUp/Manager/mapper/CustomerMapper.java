package com.stockup.StockUp.Manager.mapper;

import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.model.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
	componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = {AddressMapper.class}
)
public interface CustomerMapper {
	
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "deletedAt", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "addresses", ignore = true)
	Customer toEntity(CustomerRequestDTO dto);
	
	CustomerResponseDTO toResponse(Customer entity);
	
	CustomerSummaryDTO toSummary(Customer entity);
	
	List<CustomerResponseDTO> toResponseList(List<Customer> entities);
}

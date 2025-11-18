package com.stockup.StockUp.Manager.mapper.sales;


import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.model.sales.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {AddressMapper.class})
public interface CustomerMapper {
	
	Customer toEntity(CustomerRequestDTO dto);
	
	CustomerResponseDTO toResponse(Customer entity);
	
	CustomerSummaryDTO toSummary(Customer entity);
	
	List<CustomerResponseDTO> toResponseList(List<Customer> entities);
}

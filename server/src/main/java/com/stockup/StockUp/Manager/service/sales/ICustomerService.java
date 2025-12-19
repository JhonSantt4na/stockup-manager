package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICustomerService {
	
	CustomerResponseDTO createCustomer(CustomerRequestDTO dto);
	
	CustomerResponseDTO updateCustomer(UUID id, CustomerRequestDTO dto);
	
	CustomerResponseDTO findCustomerById(UUID id);
	
	Page<CustomerSummaryDTO> findAllCustomer(Pageable pageable);
	
	Page<CustomerSummaryDTO> findAllCustomerCustom(int page, int size, String[] sort);
	
	void softDeleteCustomer(UUID id);
	
	void enableCustomer(UUID id);
}

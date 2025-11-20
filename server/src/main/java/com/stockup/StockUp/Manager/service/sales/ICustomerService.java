package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICustomerService {
	
	CustomerResponseDTO create(CustomerRequestDTO dto);
	
	CustomerResponseDTO update(UUID id, CustomerRequestDTO dto);
	
	CustomerResponseDTO findById(UUID id);
	
	Page<CustomerSummaryDTO> findAll(Pageable pageable);
	
	Page<CustomerSummaryDTO> findAllCustom(int page, int size, String[] sort);
	
	void softDelete(UUID id);
	
	void enable(UUID id);
}

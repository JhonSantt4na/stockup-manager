package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductService {
	
	ProductResponseDTO create(ProductRequestDTO dto);
	
	ProductResponseDTO update(UUID id, ProductUpdateDTO dto);
	
	ProductResponseDTO findByName(String name);
	
	ProductResponseDTO findBySku(String sku);
	
	void delete(UUID id);
	
	Page<ProductSummaryDTO> listAll(Pageable pageable);
	
	Page<ProductSummaryDTO> listActive(Pageable pageable);
}
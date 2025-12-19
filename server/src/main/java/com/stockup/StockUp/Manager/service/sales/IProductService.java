package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductService {
	
	ProductResponseDTO createProduct(ProductRequestDTO dto);
	ProductResponseDTO updateProduct(UUID id, ProductUpdateDTO dto);
	ProductResponseDTO findProductByName(String name);
	ProductResponseDTO findProductBySku(String sku);
	void deleteProduct(UUID id);
	Page<ProductSummaryDTO> listAllProduct(Pageable pageable);
	Page<ProductSummaryDTO> listProductActive(Pageable pageable);
}
package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IBrandService {
	
	BrandResponseDTO create(BrandRequestDTO dto);
	
	BrandResponseDTO update(UUID id, BrandRequestDTO dto);
	
	void delete(UUID id);
	
	BrandResponseDTO findById(UUID id);
	
	List<BrandResponseDTO> findAll();
}
package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IBrandService {
	
	BrandResponseDTO create(BrandRequestDTO dto);
	
	BrandResponseDTO update(UUID id, BrandRequestDTO dto);
	
	void delete(UUID id);
	
	BrandResponseDTO findById(UUID id);
	
	List<BrandResponseDTO> findAll();
	
	Page<BrandResponseDTO> list(Pageable pageable);
	
}
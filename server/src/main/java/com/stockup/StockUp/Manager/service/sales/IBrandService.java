package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface IBrandService {
	BrandResponseDTO createBrand(BrandRequestDTO dto);
	BrandResponseDTO updateBrand(UUID id, BrandRequestDTO dto);
	void deleteBrand(UUID id);
	BrandResponseDTO findBrandById(UUID id);
	List<BrandResponseDTO> findAllBrand();
	Page<BrandResponseDTO> listBrand(Pageable pageable);
}
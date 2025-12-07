package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ISupplierService {
	
	SupplierResponseDTO create(SupplierRequestDTO dto);
	
	SupplierResponseDTO update(UUID id, SupplierRequestDTO dto);
	
	void delete(UUID id);
	
	SupplierResponseDTO findById(UUID id);
	
	List<SupplierResponseDTO> findAll();
	
	Page<SupplierResponseDTO> list(Pageable pageable);
}
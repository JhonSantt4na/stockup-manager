package com.stockup.StockUp.Manager.service.procurement;

import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ISupplierService {
	
	SupplierResponseDTO createSupplier(SupplierRequestDTO dto);
	SupplierResponseDTO updateSupplier(UUID id, SupplierRequestDTO dto);
	void deleteSupplier(UUID id);
	SupplierResponseDTO findSupplierById(UUID id);
	List<SupplierResponseDTO> findAllSupplier();
	Page<SupplierResponseDTO> listSupplier(Pageable pageable);
}
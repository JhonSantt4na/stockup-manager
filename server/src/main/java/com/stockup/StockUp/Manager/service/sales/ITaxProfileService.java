package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.taxProfile.TaxProfileUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface ITaxProfileService {
	
	TaxProfileResponseDTO create(TaxProfileRequestDTO dto);
	
	TaxProfileResponseDTO update(UUID id, TaxProfileUpdateDTO dto);
	
	TaxProfileResponseDTO findById(UUID id);
	
	List<TaxProfileResponseDTO> findAll();
	
	void delete(UUID id);
}
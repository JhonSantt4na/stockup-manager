package com.stockup.StockUp.Manager.service.sales;

import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileUpdateDTO;
import java.util.List;
import java.util.UUID;

public interface ITaxProfileService {
	TaxProfileResponseDTO createTaxProfile(TaxProfileRequestDTO dto);
	TaxProfileResponseDTO updateTaxProfile(UUID id, TaxProfileUpdateDTO dto);
	TaxProfileResponseDTO findTaxProfileById(UUID id);
	List<TaxProfileResponseDTO> findAllTaxProfile();
	void deleteTaxProfile(UUID id);
}
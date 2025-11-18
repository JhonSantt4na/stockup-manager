package com.stockup.StockUp.Manager.service.Others;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAddressService {
	
	AddressResponseDTO create(AddressRequestDTO dto);
	
	AddressResponseDTO update(UUID id, AddressRequestDTO dto);
	
	AddressResponseDTO findById(UUID id);
	
	Page<AddressSummaryDTO> findAll(Pageable pageable);
	
	void softDelete(UUID id);
	
	void enable(UUID id);
}

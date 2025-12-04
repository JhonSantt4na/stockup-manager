package com.stockup.StockUp.Manager.service.Others;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IAddressService {
	
	AddressResponseDTO createAddress(AddressRequestDTO dto);
	
	AddressResponseDTO updateAddress(UUID id, AddressRequestDTO dto);
	
	AddressResponseDTO findAddressById(UUID id);
	
	Page<AddressSummaryDTO> findAllAddress(Pageable pageable);
	
	void softDeleteAddress(UUID id);
	
	void enableAddress(UUID id);
}

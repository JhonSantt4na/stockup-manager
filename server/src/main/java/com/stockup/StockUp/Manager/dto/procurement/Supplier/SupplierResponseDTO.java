package com.stockup.StockUp.Manager.dto.procurement.Supplier;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressDTO;
import java.util.UUID;

public record SupplierResponseDTO(
	UUID id,
	String name,
	String cnpj,
	String email,
	String phone,
	AddressDTO address
) {}

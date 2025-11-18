package com.stockup.StockUp.Manager.dto.Others.Address;

import java.util.UUID;

public record AddressResponseDTO(
	UUID id,
	UUID customerId,
	String street,
	String number,
	String complement,
	String district,
	String city,
	String state,
	String zipCode,
	String country,
	Boolean enabled
) {}
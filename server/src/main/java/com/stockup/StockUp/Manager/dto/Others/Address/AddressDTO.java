package com.stockup.StockUp.Manager.dto.Others.Address;

import java.util.UUID;

public record AddressDTO(
	UUID id,
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

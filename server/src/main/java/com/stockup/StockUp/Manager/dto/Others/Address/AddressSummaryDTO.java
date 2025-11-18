package com.stockup.StockUp.Manager.dto.Others.Address;

import java.util.UUID;

public record AddressSummaryDTO(
	UUID id,
	String street,
	String district,
	String city,
	String state,
	String zipCode,
	Boolean enabled
) {}
package com.stockup.StockUp.Manager.dto.Others.Address;

import jakarta.validation.constraints.Size;

public record AddressUpdateDTO(
	
	@Size(max = 120)
	String street,
	
	@Size(max = 20)
	String number,
	
	@Size(max = 120)
	String complement,
	
	@Size(max = 80)
	String district,
	
	@Size(max = 80)
	String city,
	
	@Size(max = 2, message = "Use UF com 2 caracteres.")
	String state,
	
	@Size(max = 10)
	String zipCode,
	
	@Size(max = 80)
	String country
) {}
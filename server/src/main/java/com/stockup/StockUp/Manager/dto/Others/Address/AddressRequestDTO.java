package com.stockup.StockUp.Manager.dto.Others.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressRequestDTO(
	
	@NotNull(message = "The client ID is required.")
	UUID customerId,
	
	@NotBlank(message = "The street is mandatory.")
	@Size(max = 120)
	String street,
	
	@NotBlank(message = "The number is mandatory.")
	@Size(max = 20)
	String number,
	
	@Size(max = 120)
	String complement,
	
	@NotBlank(message = "The neighborhood is a must.")
	@Size(max = 80)
	String district,
	
	@NotBlank(message = "The city is a must.")
	@Size(max = 80)
	String city,
	
	@NotBlank(message = "The state is mandatory")
	@Size(max = 2, message = "Use UF com 2 caracteres.")
	String state,
	
	@NotBlank(message = "The zip code is mandatory.")
	@Size(max = 10)
	String zipCode,
	
	@NotBlank(message = "The country is mandatory")
	@Size(max = 80)
	String country
) {}
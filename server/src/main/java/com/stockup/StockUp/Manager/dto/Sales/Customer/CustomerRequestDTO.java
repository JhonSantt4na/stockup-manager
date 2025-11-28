package com.stockup.StockUp.Manager.dto.Sales.Customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequestDTO(
	@NotBlank(message = "name is required")
	@Size(max = 255)
	String name,
	
	@Size(max = 30)
	String cpfCnpj,
	
	@Size(max = 50)
	String phone,
	
	@Email
	@Size(max = 255)
	String email
) {}
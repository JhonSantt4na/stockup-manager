package com.stockup.StockUp.Manager.dto.procurement.Supplier;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierRequestDTO(
	
	@NotBlank(message = "O nome é obrigatório.")
	@Size(max = 120, message = "O nome pode ter no máximo 120 caracteres.")
	String name,
	
	@NotBlank(message = "O CNPJ é obrigatório.")
	@Size(max = 20, message = "O CNPJ pode ter no máximo 20 caracteres.")
	String cnpj,
	
	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "E-mail inválido.")
	String email,
	
	@NotBlank(message = "O telefone é obrigatório.")
	@Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres.")
	String phone,
	
	AddressDTO address
) {}
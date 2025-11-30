package com.stockup.StockUp.Manager.controller.others.Docs;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Tag(name = "Addresses", description = "Gerenciamento de endereços de clientes")
public interface AddressControllerDocs {
	
	@Operation(summary = "Criar endereço")
	AddressResponseDTO create(AddressRequestDTO dto);
	
	@Operation(summary = "Atualizar endereço")
	AddressResponseDTO update(UUID id, AddressRequestDTO dto);
	
	@Operation(summary = "Buscar endereço por ID")
	AddressResponseDTO findById(UUID id);
	
	@Operation(summary = "Listar endereços")
	Page<AddressSummaryDTO> findAll(Pageable pageable);
	
	@Operation(summary = "Desativar endereço (soft delete)")
	void softDelete(UUID id);
	
	@Operation(summary = "Reativar endereço")
	void enable(UUID id);
}

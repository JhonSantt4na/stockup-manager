package com.stockup.StockUp.Manager.service.procurement.impl;

import com.stockup.StockUp.Manager.dto.procurement.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.procurement.Supplier.SupplierResponseDTO;
import com.stockup.StockUp.Manager.mapper.procurement.SupplierMapper;
import com.stockup.StockUp.Manager.model.procurement.Supplier;
import com.stockup.StockUp.Manager.repository.procurement.SupplierRepository;
import com.stockup.StockUp.Manager.service.procurement.ISupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService implements ISupplierService {
	
	private final SupplierRepository repository;
	private final SupplierMapper mapper;
	
	@Override
	public SupplierResponseDTO create(SupplierRequestDTO dto) {
		
		if (repository.existsByCnpj(dto.cnpj())) {
			throw new IllegalArgumentException("Já existe um fornecedor com este CNPJ.");
		}
		
		if (repository.existsByEmail(dto.email())) {
			throw new IllegalArgumentException("Já existe um fornecedor com este e-mail.");
		}
		
		Supplier supplier = mapper.toEntity(dto);
		return mapper.toResponse(repository.save(supplier));
	}
	
	@Override
	public SupplierResponseDTO update(UUID id, SupplierRequestDTO dto) {
		
		Supplier supplier = repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));
		
		mapper.update(supplier, dto);
		return mapper.toResponse(repository.save(supplier));
	}
	
	@Override
	public void delete(UUID id) {
		repository.deleteById(id);
	}
	
	@Override
	public SupplierResponseDTO findById(UUID id) {
		return repository.findById(id)
			.map(mapper::toResponse)
			.orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));
	}
	
	@Override
	public List<SupplierResponseDTO> findAll() {
		return repository.findAll().stream().map(mapper::toResponse).toList();
	}
	
	@Override
	public Page<SupplierResponseDTO> list(Pageable pageable) {
		return repository.findAll(pageable).map(mapper::toResponse);
	}
}
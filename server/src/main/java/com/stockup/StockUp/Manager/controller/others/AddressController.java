package com.stockup.StockUp.Manager.controller.others;

import com.stockup.StockUp.Manager.controller.others.Docs.AddressControllerDocs;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import com.stockup.StockUp.Manager.service.Others.IAddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController implements AddressControllerDocs {
	
	private final IAddressService service;
	
	public AddressController(IAddressService service) {
		this.service = service;
	}
	
	@Override
	@PostMapping
	public AddressResponseDTO create(@RequestBody AddressRequestDTO dto) {
		return service.create(dto);
	}
	
	@Override
	@PutMapping("/{id}")
	public AddressResponseDTO update(
		@PathVariable UUID id,
		@RequestBody AddressRequestDTO dto
	) {
		return service.update(id, dto);
	}
	
	@Override
	@GetMapping("/{id}")
	public AddressResponseDTO findById(@PathVariable UUID id) {
		return service.findById(id);
	}
	
	@Override
	@GetMapping
	public Page<AddressSummaryDTO> findAll(Pageable pageable) {
		return service.findAll(pageable);
	}
	
	@Override
	@DeleteMapping("/{id}")
	public void softDelete(@PathVariable UUID id) {
		service.softDelete(id);
	}
	
	@Override
	@PatchMapping("/{id}/enable")
	public void enable(@PathVariable UUID id) {
		service.enable(id);
	}
}

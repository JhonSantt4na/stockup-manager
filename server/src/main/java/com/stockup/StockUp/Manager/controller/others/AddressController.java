package com.stockup.StockUp.Manager.controller.others;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.others.Docs.AddressControllerDocs;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.Others.IAddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController implements AddressControllerDocs {
	
	private final IAddressService service;
	
	@Override
	@PostMapping("/createCashMovement")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressRequestDTO dto) {
		try {
			AddressResponseDTO created = service.createAddress(dto);
			AuditLogger.log("ADDRESS_CREATE", getCurrentUser(), "SUCCESS",
				"Address creating to: " + dto.street());
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
			
		} catch (DuplicateResourceException e) {
			AuditLogger.log("ADDRESS_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
			
		} catch (Exception e) {
			AuditLogger.log("ADDRESS_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			throw new RuntimeException("Erro creating Address", e);
		}
	}
	
	@Override
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<AddressResponseDTO> updateAddress(
		@PathVariable @NotNull UUID id,
		@Valid @RequestBody AddressRequestDTO dto) {
		
		AddressResponseDTO updated = service.updateAddress(id, dto);
		AuditLogger.log("ADDRESS_UPDATE", getCurrentUser(), "SUCCESS",
			"Address Updated: " + id);
		
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<AddressResponseDTO> findAddressById(@PathVariable @NotNull UUID id) {
		AddressResponseDTO response = service.findAddressById(id);
		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping()
	public ResponseEntity<Page<AddressSummaryDTO>> findAllAddress(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = buildPageableAddress(page, size, sort);
		Page<AddressSummaryDTO> result = service.findAllAddress(pageable);
		
		return ResponseEntity.ok(result);
	}
	
	@Override
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> softDeleteAddress(@PathVariable UUID id) {
		service.softDeleteAddress(id);
		AuditLogger.log("ADDRESS_DELETE", getCurrentUser(), "SUCCESS",
			"Address Disabled: " + id);
		
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PatchMapping("/{id}/enableAddress")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> enableAddress(@PathVariable UUID id) {
		service.enableAddress(id);
		AuditLogger.log("ADDRESS_ENABLE", getCurrentUser(), "SUCCESS",
			"Address Enabled: " + id);
		
		return ResponseEntity.noContent().build();
	}
	
	private Pageable buildPageableAddress(int page, int size, String[] sort) {
		String field = "createdAt";
		Sort.Direction direction = Sort.Direction.DESC;
		
		if (sort.length > 0 && !sort[0].isBlank()) {
			field = sort[0];
		}
		if (sort.length > 1 && sort[1].equalsIgnoreCase("asc")) {
			direction = Sort.Direction.ASC;
		}
		
		return PageRequest.of(page, size, Sort.by(direction, field));
	}
}
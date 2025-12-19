package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.CustomerControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController implements CustomerControllerDocs {
	
	private final ICustomerService service;
	
	@Override
	@PostMapping("/createCashMovement")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<CustomerResponseDTO> createCustomer(
		@Valid @RequestBody CustomerRequestDTO dto) {
		
		try {
			CustomerResponseDTO created = service.createCustomer(dto);
			
			AuditLogger.log("CUSTOMER_CREATE", getCurrentUser(), "SUCCESS",
				"Customer Created: " + dto.name());
			
			return ResponseEntity
				.created(URI.create("/api/v1/customers/" + created.id()))
				.body(created);
			
		} catch (DuplicateResourceException e) {
			
			AuditLogger.log("CUSTOMER_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
			
		} catch (Exception e) {
			
			AuditLogger.log("CUSTOMER_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			throw new RuntimeException("Erro Creating Customer", e);
		}
	}
	
	@Override
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<CustomerResponseDTO> updateCustomer(
		@PathVariable UUID id,
		@Valid @RequestBody CustomerRequestDTO dto) {
		
		CustomerResponseDTO updated = service.updateCustomer(id, dto);
		
		AuditLogger.log("CUSTOMER_UPDATE", getCurrentUser(), "SUCCESS",
			"Customer Updating: " + id);
		
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable UUID id) {
		CustomerResponseDTO response = service.findCustomerById(id);
		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Page<CustomerSummaryDTO>> listCustomer(Pageable pageable) {
		Page<CustomerSummaryDTO> result = service.findAllCustomer(pageable);
		return ResponseEntity.ok(result);
	}
	
	
	@Override
	@GetMapping("/custom")
	public ResponseEntity<Page<CustomerSummaryDTO>> listCustomCustomer(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Page<CustomerSummaryDTO> result = service.findAllCustomerCustom(page, size, sort);
		return ResponseEntity.ok(result);
	}
	
	@Override
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
		
		service.softDeleteCustomer(id);
		
		AuditLogger.log("CUSTOMER_DELETE", getCurrentUser(), "SUCCESS",
			"Customer Disabled: " + id);
		
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PostMapping("/{id}/enableCustomer")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> enableCustomer(@PathVariable UUID id) {
		
		service.enableCustomer(id);
		
		AuditLogger.log("CUSTOMER_ENABLE", getCurrentUser(), "SUCCESS",
			"Customer Enabled: " + id);
		
		return ResponseEntity.noContent().build();
	}
	
	private Pageable buildPageable(int page, int size, String[] sort) {
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
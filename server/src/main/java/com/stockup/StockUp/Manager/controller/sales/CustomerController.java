package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.controller.sales.docs.CustomerControllerDocs;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Customer.CustomerSummaryDTO;
import com.stockup.StockUp.Manager.service.sales.impl.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController implements CustomerControllerDocs {
	
	private final CustomerService service;
	
	public CustomerController(CustomerService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO dto) {
		CustomerResponseDTO created = service.create(dto);
		return ResponseEntity.created(URI.create("/api/v1/customers/" + created.id())).body(created);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> update(
		@PathVariable UUID id,
		@Valid @RequestBody CustomerRequestDTO dto) {
		return ResponseEntity.ok(service.update(id, dto));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponseDTO> getById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@GetMapping
	public ResponseEntity<Page<CustomerSummaryDTO>> list(Pageable pageable) {
		Page<CustomerSummaryDTO> result = service.findAll(pageable);
		return ResponseEntity.ok(result);
	}
	
	@Override
	@GetMapping("/custom")
	public ResponseEntity<Page<CustomerSummaryDTO>> listCustom(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort
	) {
		Page<CustomerSummaryDTO> result = service.findAllCustom(page, size, sort);
		return ResponseEntity.ok(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.softDelete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/enable")
	public ResponseEntity<Void> enable(@PathVariable UUID id) {
		service.enable(id);
		return ResponseEntity.noContent().build();
	}
}

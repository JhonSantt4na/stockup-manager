package com.stockup.StockUp.Manager.controller.supply;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.supply.docs.SupplierControllerDocs;
import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierRequestDTO;
import com.stockup.StockUp.Manager.dto.supply.Supplier.SupplierResponseDTO;
import com.stockup.StockUp.Manager.service.procurement.ISupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/v1/supply/suppliers")
@RequiredArgsConstructor
public class SupplierController implements SupplierControllerDocs {
	
	private final ISupplierService service;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/createCashMovement")
	public ResponseEntity<SupplierResponseDTO> createSupplier(@RequestBody SupplierRequestDTO dto) {
		SupplierResponseDTO response = service.createSupplier(dto);
		AuditLogger.log("SUPPLIER_CREATE", getCurrentUser(), "SUCCESS", "Supplier created: " + dto.name());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatePaymentMethod/{id}")
	public ResponseEntity<SupplierResponseDTO> updateSupplier(@PathVariable UUID id, @RequestBody SupplierRequestDTO dto) {
		SupplierResponseDTO updated = service.updateSupplier(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<SupplierResponseDTO> findSupplierById(@PathVariable UUID id) {
		return ResponseEntity.ok(service.findSupplierById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteCashMovement/{id}")
	public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {
		service.deleteSupplier(id);
		AuditLogger.log("SUPPLIER_DELETE", getCurrentUser(), "SUCCESS", "Supplier deleted: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<Page<SupplierResponseDTO>> listSuppliers(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "name,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<SupplierResponseDTO> response = service.listSupplier(pageable);
		return ResponseEntity.ok(response);
	}
}

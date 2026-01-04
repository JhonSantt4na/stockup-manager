package com.stockup.StockUp.Manager.controller.catalog;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.catalog.docs.BrandControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Brand.BrandResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.IBrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController implements BrandControllerDocs {
	
	private final IBrandService brandService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/createCashMovement")
	public ResponseEntity<BrandResponseDTO> createBrand(@RequestBody @Valid BrandRequestDTO dto) {
		try {
			BrandResponseDTO response = brandService.createBrand(dto);
			AuditLogger.log("BRAND_CREATE", getCurrentUser(), "SUCCESS", "Brand created: " + dto.name());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("BRAND_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("BRAND_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			throw new RuntimeException("Error creating brand", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/updatePaymentMethod/{id}")
	public ResponseEntity<BrandResponseDTO> updateBrand(@PathVariable UUID id, @RequestBody @Valid BrandRequestDTO dto) {
		BrandResponseDTO updated = brandService.updateBrand(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<BrandResponseDTO> findBrandById(@PathVariable @NotNull UUID id) {
		return ResponseEntity.ok(brandService.findBrandById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteCashMovement/{id}")
	public ResponseEntity<Void> deleteBrand(@PathVariable @NotNull UUID id) {
		brandService.deleteBrand(id);
		AuditLogger.log("BRAND_DELETE", getCurrentUser(), "SUCCESS", "Brand deleted: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<Page<BrandResponseDTO>> listBrands(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "name,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<BrandResponseDTO> response = brandService.listBrand(pageable);
		return ResponseEntity.ok(response);
	}
}
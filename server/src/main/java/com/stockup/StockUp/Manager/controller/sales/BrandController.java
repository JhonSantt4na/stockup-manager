package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.BrandControllerDocs;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Brand.BrandResponseDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.impl.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController implements BrandControllerDocs {
	
	private final BrandService brandService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<BrandResponseDTO> create(@RequestBody BrandRequestDTO dto) {
		try {
			BrandResponseDTO response = brandService.create(dto);
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
	@PutMapping("/update/{id}")
	public ResponseEntity<BrandResponseDTO> update(@PathVariable UUID id, @RequestBody BrandRequestDTO dto) {
		BrandResponseDTO updated = brandService.update(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<BrandResponseDTO> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(brandService.findById(id));
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		brandService.delete(id);
		AuditLogger.log("BRAND_DELETE", getCurrentUser(), "SUCCESS", "Brand deleted: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/list")
	public ResponseEntity<Page<BrandResponseDTO>> listBrands(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "name,asc") String[] sort
	) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort[0])));
		Page<BrandResponseDTO> response = brandService.list(pageable);
		return ResponseEntity.ok(response);
	}
}

package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.TaxProfileControllerDocs;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.taxProfile.TaxProfileUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.impl.TaxProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/tax-profiles")
@RequiredArgsConstructor
public class TaxProfileController implements TaxProfileControllerDocs {
	
	private final TaxProfileService taxProfileService;
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<TaxProfileResponseDTO> createTaxProfile(@Valid @RequestBody TaxProfileRequestDTO dto) {
		try {
			TaxProfileResponseDTO created = taxProfileService.create(dto);
			AuditLogger.log("TAX_PROFILE_CREATE", getCurrentUser(), "SUCCESS",
				"Tax profile created: " + dto.getName());
			return ResponseEntity.status(HttpStatus.CREATED).body(created);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("TAX_PROFILE_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("TAX_PROFILE_CREATE", getCurrentUser(), "FAILED",
				"Error creating tax profile: " + e.getMessage());
			throw new RuntimeException("Error creating tax profile", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<TaxProfileResponseDTO> updateTaxProfile(@PathVariable UUID id,
																  @Valid @RequestBody TaxProfileUpdateDTO dto) {
		try {
			TaxProfileResponseDTO updated = taxProfileService.update(id, dto);
			AuditLogger.log("TAX_PROFILE_UPDATE", getCurrentUser(), "SUCCESS",
				"Tax profile updated: " + id);
			return ResponseEntity.ok(updated);
		} catch (EntityNotFoundException e) {
			AuditLogger.log("TAX_PROFILE_UPDATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			AuditLogger.log("TAX_PROFILE_UPDATE", getCurrentUser(), "FAILED",
				"Error updating tax profile: " + e.getMessage());
			throw new RuntimeException("Error updating tax profile", e);
		}
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<TaxProfileResponseDTO> getTaxProfileById(@PathVariable UUID id) {
		TaxProfileResponseDTO profile = taxProfileService.findById(id);
		return ResponseEntity.ok(profile);
	}
	
	@Override
	@GetMapping
	public ResponseEntity<List<TaxProfileResponseDTO>> listTaxProfiles() {
		List<TaxProfileResponseDTO> profiles = taxProfileService.findAll();
		return ResponseEntity.ok(profiles);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTaxProfile(@PathVariable UUID id) {
		try {
			taxProfileService.delete(id);
			AuditLogger.log("TAX_PROFILE_DELETE", getCurrentUser(), "SUCCESS",
				"Tax profile deleted: " + id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			AuditLogger.log("TAX_PROFILE_DELETE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			AuditLogger.log("TAX_PROFILE_DELETE", getCurrentUser(), "FAILED",
				"Error deleting tax profile: " + e.getMessage());
			throw new RuntimeException("Error deleting tax profile", e);
		}
	}
}
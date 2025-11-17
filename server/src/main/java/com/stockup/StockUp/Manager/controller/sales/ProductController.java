package com.stockup.StockUp.Manager.controller.sales;

import com.stockup.StockUp.Manager.audit.AuditLogger;
import com.stockup.StockUp.Manager.controller.sales.docs.ProductControllerDocs;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.sales.Product.ProductUpdateDTO;
import com.stockup.StockUp.Manager.exception.DuplicateResourceException;
import com.stockup.StockUp.Manager.service.sales.impl.ProductServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.stockup.StockUp.Manager.util.WebClient.getCurrentUser;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs {
	
	private final ProductServices productService;
	
	@Override
	public ResponseEntity<ProductResponseDTO> createProduct(ProductRequestDTO dto) {
		try {
			ProductResponseDTO product = productService.create(dto);
			AuditLogger.log("PRODUCT_CREATE", getCurrentUser(), "SUCCESS", "Produto criado: " + dto.getName());
			return ResponseEntity.status(HttpStatus.CREATED).body(product);
		} catch (DuplicateResourceException e) {
			AuditLogger.log("PRODUCT_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch (Exception e) {
			AuditLogger.log("PRODUCT_CREATE", getCurrentUser(), "FAILED", e.getMessage());
			throw new RuntimeException("Erro ao criar produto", e);
		}
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable UUID id,
															@Valid @RequestBody ProductUpdateDTO dto) {
		ProductResponseDTO updated = productService.update(id, dto);
		AuditLogger.log("PRODUCT_UPDATE", getCurrentUser(), "SUCCESS", "Produto atualizado: " + id);
		return ResponseEntity.ok(updated);
	}
	
	@Override
	@GetMapping("/by-name/{name}")
	public ResponseEntity<ProductResponseDTO> getProductByName(@PathVariable String name) {
		ProductResponseDTO product = productService.findByName(name);
		return ResponseEntity.ok(product);
	}

	@Override
	@GetMapping("/by-sku/{sku}")
	public ResponseEntity<ProductResponseDTO> getProductBySku(@PathVariable String sku) {
		ProductResponseDTO product = productService.findBySku(sku);
		return ResponseEntity.ok(product);
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
		productService.delete(id);
		AuditLogger.log("PRODUCT_DELETE", getCurrentUser(), "SUCCESS", "Produto desativado: " + id);
		return ResponseEntity.noContent().build();
	}
	
	@Override
	@GetMapping
	public ResponseEntity<Page<ProductSummaryDTO>> listProducts(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));
		Page<ProductSummaryDTO> response = productService.listAll(pageable);
		return ResponseEntity.ok(response);
	}
	
	@Override
	@GetMapping("/active")
	public ResponseEntity<Page<ProductSummaryDTO>> listActiveProducts(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt,desc") String[] sort) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));
		Page<ProductSummaryDTO> response = productService.listActive(pageable);
		return ResponseEntity.ok(response);
	}
}
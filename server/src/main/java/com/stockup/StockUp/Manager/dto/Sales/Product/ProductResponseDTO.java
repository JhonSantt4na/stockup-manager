package com.stockup.StockUp.Manager.dto.Sales.Product;

import com.stockup.StockUp.Manager.Enums.ProductOrigin;
import com.stockup.StockUp.Manager.Enums.ProductStatus;
import com.stockup.StockUp.Manager.model.catalog.UnitOfMeasure;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {
	
	private UUID id;
	private String name;
	private String sku;
	private String gtin;
	private String description;
	private UnitOfMeasure unitOfMeasure;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	
	private String cst;
	private String ncm;
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	private UUID categoryId;
	private String categoryName;
	
	private UUID taxProfileId;
	private String taxProfileName;
	
	private ProductOrigin origin;
	private ProductStatus status;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean enabled;
}

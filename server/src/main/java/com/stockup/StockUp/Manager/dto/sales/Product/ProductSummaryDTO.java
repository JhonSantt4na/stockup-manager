package com.stockup.StockUp.Manager.dto.sales.Product;

import com.stockup.StockUp.Manager.Enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductSummaryDTO {
	
	private UUID id;
	private String name;
	private String sku;
	private BigDecimal salePrice;
	private ProductStatus status;
}
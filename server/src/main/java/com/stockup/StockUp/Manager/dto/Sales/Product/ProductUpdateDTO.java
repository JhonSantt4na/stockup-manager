package com.stockup.StockUp.Manager.dto.Sales.Product;

import com.stockup.StockUp.Manager.Enums.ProductOrigin;
import com.stockup.StockUp.Manager.Enums.ProductStatus;
import com.stockup.StockUp.Manager.model.catalog.UnitOfMeasure;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductUpdateDTO {

	private String name;
	private String sku;
	private String gtin;
	private String description;
	private UnitOfMeasure unitOfMeasure;
	
	@DecimalMin(value = "0.00", message = "O preço de custo deve ser positivo.")
	private BigDecimal costPrice;
	
	@DecimalMin(value = "0.00", message = "O preço de venda deve ser positivo.")
	private BigDecimal salePrice;
	
	private String cst;
	private String ncm;
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	private UUID categoryId;
	private UUID taxProfileId;
	
	private ProductOrigin origin;
	private ProductStatus status;
}
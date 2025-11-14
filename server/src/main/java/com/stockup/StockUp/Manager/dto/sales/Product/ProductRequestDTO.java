package com.stockup.StockUp.Manager.dto.sales.Product;

import com.stockup.StockUp.Manager.Enums.ProductOrigin;
import com.stockup.StockUp.Manager.Enums.ProductStatus;
import com.stockup.StockUp.Manager.Enums.UnitOfMeasure;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ProductRequestDTO {
	
	@NotBlank(message = "O nome do produto é obrigatório.")
	private String name;
	
	@NotBlank(message = "O SKU é obrigatório.")
	private String sku;
	
	private String gtin;
	
	private String description;
	
	@NotNull(message = "A unidade de medida é obrigatória.")
	private UnitOfMeasure unitOfMeasure;
	
	@NotNull(message = "O preço de custo é obrigatório.")
	@DecimalMin(value = "0.00", message = "O preço de custo deve ser positivo.")
	private BigDecimal costPrice;
	
	@NotNull(message = "O preço de venda é obrigatório.")
	@DecimalMin(value = "0.00", message = "O preço de venda deve ser positivo.")
	private BigDecimal salePrice;
	
	private String cst;
	private String ncm;
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	@NotNull(message = "A categoria é obrigatória.")
	private UUID categoryId;
	
	@NotNull(message = "O perfil fiscal é obrigatório.")
	private UUID taxProfileId;
	
	@NotNull(message = "A origem do produto é obrigatória.")
	private ProductOrigin origin;
	
	@NotNull(message = "O status do produto é obrigatório.")
	private ProductStatus status;
}

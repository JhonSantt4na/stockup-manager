package com.stockup.StockUp.Manager.model.catalog;

import com.stockup.StockUp.Manager.Enums.ProductOrigin;
import com.stockup.StockUp.Manager.Enums.ProductStatus;
import com.stockup.StockUp.Manager.Enums.UnitOfMeasure;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@Column(nullable = false, unique = true, length = 50)
	private String sku;   // código interno
	
	@Column(length = 20)
	private String gtin;  // Código de barras
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Enumerated(EnumType.STRING)
	private UnitOfMeasure unitOfMeasure;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal costPrice;
	
	@Column(precision = 19, scale = 4)
	private BigDecimal salePrice;
	
	@Column(length = 10)
	private String cst;
	
	@Column(length = 10)
	private String ncm;
	
	@Column(length = 10)
	private String cfop;
	
	@Column(precision = 5, scale = 2)
	private BigDecimal icmsRate;
	
	@Column(precision = 5, scale = 2)
	private BigDecimal pisRate;
	
	@Column(precision = 5, scale = 2)
	private BigDecimal cofinsRate;
	
	@Column(precision = 5, scale = 2)
	private BigDecimal ipiRate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_profile_id")
	private TaxProfile taxProfile;
	
	@Enumerated(EnumType.STRING)
	private ProductOrigin origin;
	
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	@Override
	public String toString() {
		return String.format("%s (%s) - %s", name, sku, status);
	}
}
package com.stockup.StockUp.Manager.model.sales;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "tax_profiles")
public class TaxProfile extends BaseEntity {
	
	@Column(nullable = false, unique = true, length = 100)
	private String name; // Ex: "Varejo Nacional", "Simples Nacional Alimentos"
	
	@Column(length = 30)
	private String regime; // Ex: "SIMPLES_NACIONAL", "LUCRO_PRESUMIDO"
	
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
	
//	@OneToMany(mappedBy = "taxProfile")
//	private List<Product> products;
	
	@Override
	public String toString() {
		return String.format("%s [%s - NCM: %s]", name, regime, ncm);
	}
}
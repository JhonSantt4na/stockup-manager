package com.stockup.StockUp.Manager.dto.Sales.taxProfile;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TaxProfileUpdateDTO {
	
	@Size(max = 100, message = "Name cannot exceed 100 characters")
	private String name;
	
	@Size(max = 10, message = "CST cannot exceed 10 characters")
	private String cst;
	
	@Size(max = 10, message = "NCM cannot exceed 10 characters")
	private String ncm;
	
	@Size(max = 10, message = "CFOP cannot exceed 10 characters")
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	@Size(max = 50, message = "Tax regime cannot exceed 50 characters")
	private String regime;
}
package com.stockup.StockUp.Manager.dto.sales.taxProfile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TaxProfileRequestDTO {
	
	@NotBlank
	@Size(max = 100)
	private String name;
	
	private String cst;
	private String ncm;
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	private String regime;
}
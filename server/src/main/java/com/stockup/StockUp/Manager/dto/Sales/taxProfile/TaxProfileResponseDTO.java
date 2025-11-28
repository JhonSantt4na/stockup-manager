package com.stockup.StockUp.Manager.dto.Sales.taxProfile;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TaxProfileResponseDTO {
	
	private UUID id;
	private String name;
	private String cst;
	private String ncm;
	private String cfop;
	
	private BigDecimal icmsRate;
	private BigDecimal pisRate;
	private BigDecimal cofinsRate;
	private BigDecimal ipiRate;
	
	private String regime;
	private boolean enabled;
}
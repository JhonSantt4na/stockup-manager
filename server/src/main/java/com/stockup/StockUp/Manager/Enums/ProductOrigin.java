package com.stockup.StockUp.Manager.Enums;

public enum ProductOrigin {
	NATIONAL("Produto nacional"),
	FOREIGN_IMPORTED_DIRECTLY("Importado diretamente"),
	FOREIGN_ACQUIRED_DOMESTIC_MARKET("Importado adquirido no mercado interno");
	
	private final String description;
	
	ProductOrigin(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}

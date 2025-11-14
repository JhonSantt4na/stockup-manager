package com.stockup.StockUp.Manager.Enums;

public enum ProductStatus {
	ACTIVE("Produto ativo"),
	INACTIVE("Produto inativo"),
	DRAFT( "Rascunho – produto ainda não publicado"),
	OUT_OF_STOCK( "Produto sem estoque – não pode ser vendido"),
	DISCONTINUED("Produto descontinuado");
	
	private final String description;
	
	ProductStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
package com.stockup.StockUp.Manager.Enums;

public enum UnitOfMeasure {
	UN("Unidade"),
	KG("Quilograma"),
	L("Litro"),
	M("Metro"),
	CX("Caixa"),
	PC("Pacote");
	
	private final String description;
	
	UnitOfMeasure(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
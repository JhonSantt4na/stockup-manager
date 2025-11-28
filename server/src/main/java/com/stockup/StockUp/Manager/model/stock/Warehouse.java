package com.stockup.StockUp.Manager.model.stock;

import com.stockup.StockUp.Manager.Enums.Stock.WarehouseType;
import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "warehouses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse extends BaseEntity {
	
	@Column(nullable = false, length = 120)
	@NotBlank(message = "O nome do local é obrigatório.")
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private WarehouseType type = WarehouseType.STORE;
	
	@Column(nullable = false)
	private Boolean isDefault = false;
	
	@Column(length = 255)
	private String address;
}
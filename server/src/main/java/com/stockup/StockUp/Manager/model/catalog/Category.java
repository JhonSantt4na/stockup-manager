package com.stockup.StockUp.Manager.model.catalog;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {
	
	@Column(nullable = false, unique = true, length = 100)
	private String name;
	
	@Column(length = 255)
	private String description;
	
	public Category() {
	}
}
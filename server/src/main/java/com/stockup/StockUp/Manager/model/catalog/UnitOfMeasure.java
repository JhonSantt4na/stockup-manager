package com.stockup.StockUp.Manager.model.catalog;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "units_of_measure")
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasure extends BaseEntity {
	
	@Column(nullable = false, length = 10, unique = true)
	@NotBlank(message = "A abreviação é obrigatória.")
	@Size(max = 10, message = "A abreviação deve ter no máximo 10 caracteres.")
	private String abbreviation;
	
	@Column(nullable = false, length = 150)
	@NotBlank(message = "A descrição é obrigatória.")
	private String description;
}
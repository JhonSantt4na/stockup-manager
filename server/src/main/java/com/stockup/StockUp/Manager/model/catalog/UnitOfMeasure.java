package com.stockup.StockUp.Manager.model.catalog;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "units_of_measure")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UnitOfMeasure extends BaseEntity {
	
	@Column(nullable = false, length = 10, unique = true)
	@NotBlank(message = "A abreviação é obrigatória.")
	@Size(max = 10, message = "A abreviação deve ter no máximo 10 caracteres.")
	private String abbreviation;
	
	@Column(nullable = false, length = 150)
	@NotBlank(message = "A descrição é obrigatória.")
	private String description;
}

package com.stockup.StockUp.Manager.model.catalog;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "brands")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Brand extends BaseEntity {
	
	@Column(nullable = false, unique = true, length = 100)
	@NotBlank(message = "O nome da marca é obrigatório.")
	private String name;
}

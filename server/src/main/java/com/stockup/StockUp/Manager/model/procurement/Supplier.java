package com.stockup.StockUp.Manager.model.procurement;

import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.customer.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supplier")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier extends BaseEntity {
	
	@Column(nullable = false, length = 120)
	private String name;
	
	@Column(nullable = false, unique = true, length = 20)
	private String cnpj;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false, length = 20)
	private String phone;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;
}
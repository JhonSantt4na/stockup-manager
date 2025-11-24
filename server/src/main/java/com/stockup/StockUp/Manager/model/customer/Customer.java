package com.stockup.StockUp.Manager.model.customer;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(name = "cpf_cnpj", length = 30)
	private String cpfCnpj;
	
	@Column(length = 50)
	private String phone;
	
	@Column(length = 255)
	private String email;
	
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Address> addresses = new LinkedHashSet<>();
	
	public void addAddress(Address address) {
		address.setCustomer(this);
		this.addresses.add(address);
	}
	
	public void removeAddress(Address address) {
		address.setCustomer(null);
		this.addresses.remove(address);
	}
}

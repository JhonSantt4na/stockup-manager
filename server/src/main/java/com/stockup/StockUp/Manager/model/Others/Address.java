package com.stockup.StockUp.Manager.model.Others;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.sales.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	private String street;
	private String number;
	private String complement;
	private String district;
	private String city;
	private String state;
	private String zipCode;
	private String country;
}

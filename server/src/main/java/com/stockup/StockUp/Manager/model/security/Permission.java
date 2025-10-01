package com.stockup.StockUp.Manager.model.security;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Getter
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity implements GrantedAuthority {
	
	@Column(unique = true)
	private String description;
	
	@Override
	public String getAuthority() {
		return this.description;
	}
}
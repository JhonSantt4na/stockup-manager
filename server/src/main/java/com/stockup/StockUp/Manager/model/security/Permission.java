package com.stockup.StockUp.Manager.model.security;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity implements GrantedAuthority {
	
	@Column(unique = true, nullable = false)
	private String description;
	
	@ManyToMany(mappedBy = "permissions")
	private Set<Role> roles = new HashSet<>();
	
	@Override
	public String getAuthority() {
		return this.description;
	}
}
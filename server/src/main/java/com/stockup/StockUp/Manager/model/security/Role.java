package com.stockup.StockUp.Manager.model.security;

import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "role_permission",
		joinColumns = @JoinColumn(name = "id_role"),
		inverseJoinColumns = @JoinColumn(name = "id_permission")
	)
	private List<Permission> permissions = new ArrayList<>();
	
	@Override
	public String getAuthority() {
		return name;
	}
}
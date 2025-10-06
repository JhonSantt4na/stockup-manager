package com.stockup.StockUp.Manager.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stockup.StockUp.Manager.model.BaseEntity;
import com.stockup.StockUp.Manager.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "roles")
@AllArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
	
	@Column(unique = true, nullable = false)
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinTable(
		name = "role_permission",
		joinColumns = @JoinColumn(name = "id_role"),
		inverseJoinColumns = @JoinColumn(name = "id_permission")
	)
	private List<Permission> permissions = new ArrayList<>();
	
	public Role() {}
	
	public Role(String name) {
		this.name = name;
	}
	
	@Override
	public String getAuthority() {
		return "ROLE_" + name;
	}
}
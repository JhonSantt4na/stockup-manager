package com.stockup.StockUp.Manager.model;

import com.stockup.StockUp.Manager.model.security.Permission;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
	
	@Column(name = "full_name", nullable = false)
	private String fullName;
	
	@NotNull(message = "username cannot be null")
	@Column(unique = true, nullable = false)
	private String username;
	
	@Email
	@NotNull(message = "Email cannot be null")
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_permission",
		joinColumns = @JoinColumn(name = "id_user"),
		inverseJoinColumns = @JoinColumn(name = "id_permission")
	)
	private List<Permission> permissions = new ArrayList<>();
	
	@Column(name = "last_activity")
	private LocalDateTime lastActivity;
	
	@Column(name = "account_non_expired")
	private boolean accountNonExpired = true;
	
	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;
	
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired = true;
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		if (permissions != null) {
			permissions.forEach(p -> roles.add(p.getDescription()));
		}
		return roles;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return permissions;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}
}
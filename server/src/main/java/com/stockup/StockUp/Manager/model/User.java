package com.stockup.StockUp.Manager.model;

import com.stockup.StockUp.Manager.model.security.Role;
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
	
	@NotNull(message = "Username cannot be null")
	@Column(unique = true, nullable = false)
	private String username;
	
	@NotNull(message = "Email cannot be null")
	@Email
	@Column(unique = true, nullable = false)
	private String email;
	
	@NotNull
	@Column(nullable = false)
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_role",
		joinColumns = @JoinColumn(name = "id_user"),
		inverseJoinColumns = @JoinColumn(name = "id_role")
	)
	private List<Role> roles = new ArrayList<>();
	
	@Column(name = "last_activity")
	private LocalDateTime lastActivity;
	
	@Column(name = "account_non_expired")
	private boolean accountNonExpired = true;
	
	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;
	
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired = true;
	
	@Column(name = "enabled")
	private boolean enabled = true;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
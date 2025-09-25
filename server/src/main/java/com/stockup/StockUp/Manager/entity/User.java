package com.stockup.StockUp.Manager.entity;


import com.stockup.StockUp.Manager.entity.security.Permission;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails,Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;
	
	@NotNull(message = "username cannot be null")
	private String username;
	
	@Email
	@NotNull(message = "Email cannot be null")
	private String email;
	
	private String password_hash;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_permission",
		joinColumns = @JoinColumn(name = "id_user"),
		inverseJoinColumns = @JoinColumn(name = "id_permission")
	)
	private List<Permission> permissions = new ArrayList<>();
	
	@CreationTimestamp
	private Timestamp created_at;
	
	@UpdateTimestamp
	private Timestamp updated_at;
	
	private LocalDateTime deletedAt;
	
	@Column(name = "account_non_expired")
	private boolean accountNonExpired = true;
	
	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;
	
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired = true;
	
	@Column
	private boolean enabled = true;
	
	
	public boolean isDeleted() {
		return deletedAt != null;
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		if (permissions != null) {
			permissions.forEach(p -> roles.add(p.getDescription()));
		}
		return roles;
	}
	
	@Override
	public String getPassword() {
		return this.password_hash;
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
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
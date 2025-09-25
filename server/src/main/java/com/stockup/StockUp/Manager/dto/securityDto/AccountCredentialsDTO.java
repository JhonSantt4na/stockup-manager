package com.stockup.StockUp.Manager.dto.securityDto;

import com.stockup.StockUp.Manager.entity.security.Permission;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class AccountCredentialsDTO implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Full name is required")
	private String fullName;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;
	
	@NotBlank(message = "Password is required")
	private String password;
	
	private List<Permission> permissions = new ArrayList<>();
	
	public AccountCredentialsDTO(String userName, String password, String fullName, String email) {
	}
	
	public AccountCredentialsDTO() {
		this.permissions = new ArrayList<>();
	}
	
	public List<String> getRoles() {
		List<String> roles = new ArrayList<>();
		if (permissions != null) {
			for (Permission permission : permissions) {
				roles.add(permission.getDescription());
			}
		}
		return roles;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AccountCredentialsDTO that = (AccountCredentialsDTO) o;
		return Objects.equals(username, that.username) &&
			Objects.equals(fullName, that.fullName) &&
			Objects.equals(email, that.email) &&
			Objects.equals(password, that.password) &&
			Objects.equals(permissions, that.permissions);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(username, fullName, email, password, permissions);
	}
}
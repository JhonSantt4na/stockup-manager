package com.stockup.StockUp.Manager.model.security;

import com.stockup.StockUp.Manager.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "permission")
public class Permission extends BaseEntity implements GrantedAuthority, Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Column(unique = true)
	private String description;
	
	@Override
	public String getAuthority() {
		return this.description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Permission that = (Permission) o;
		return Objects.equals(description, that.description);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(description);
	}
}
package com.stockup.StockUp.Manager.service.auth;

import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
	Role createRole(RoleDTO createDto);
	Role updateRole(RoleUpdateDTO updateDto);
	Role getRoleById(UUID id);
	Role getRoleByName(String name);
	void deleteRole(String name);
	Page<Role> getAllRoles(Pageable pageable, String search);
	Page<Role> getAllRolesWithUsers(Pageable pageable, String search);
	Role assignPermissions(String roleName, List<String> permissionDescriptions);
	Role removePermissions(String roleName, List<String> permissionDescriptions);
	List<String> getRolePermissions(String roleName);
}
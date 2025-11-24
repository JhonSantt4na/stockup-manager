package com.stockup.StockUp.Manager.service.auth;

import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.permission.response.PermissionWithRolesDTO;
import com.stockup.StockUp.Manager.model.user.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPermissionService {
	
	Permission createPermission(PermissionCreateDTO dto);
	
	Permission updatePermission(PermissionUpdateDTO dto);
	
	void deletePermission(String description);
	
	Page<PermissionWithRolesDTO> getAllActivePermissions(Pageable pageable);
	
	Page<PermissionWithRolesDTO> getAllPermissions(Pageable pageable);
	
	Permission getPermissionByDescription(String description);
}
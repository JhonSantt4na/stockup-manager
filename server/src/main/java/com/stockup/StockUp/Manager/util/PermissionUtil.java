package com.stockup.StockUp.Manager.util;

import com.stockup.StockUp.Manager.model.security.Permission;
import com.stockup.StockUp.Manager.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionUtil {
	
	@Autowired
	private PermissionRepository permissionsRepository;
	
	private Permission create(String description) {
		Permission newPermission = new Permission();
		newPermission.setDescription(description);
		return permissionsRepository.save(newPermission);
	}
}
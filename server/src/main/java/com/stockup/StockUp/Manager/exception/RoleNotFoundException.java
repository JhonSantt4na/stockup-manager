package com.stockup.StockUp.Manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(String description) {
		super("Role não encontrado: " + description);
	}
}
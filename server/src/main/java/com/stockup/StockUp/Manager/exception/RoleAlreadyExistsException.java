package com.stockup.StockUp.Manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleAlreadyExistsException extends RuntimeException {
	public RoleAlreadyExistsException(String description) {
		super("A role do usuário '" + description + "' já está em uso.");
	}
}
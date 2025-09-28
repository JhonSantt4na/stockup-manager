package com.stockup.StockUp.Manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameAlreadyExistsException extends RuntimeException {
	public UsernameAlreadyExistsException(String username) {
		super("O nome de usuário '" + username + "' já está em uso.");
	}
}
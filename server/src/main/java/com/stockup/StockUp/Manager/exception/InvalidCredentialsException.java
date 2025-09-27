package com.stockup.StockUp.Manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException() {
		super("Credenciais inválidas.");
	}
}

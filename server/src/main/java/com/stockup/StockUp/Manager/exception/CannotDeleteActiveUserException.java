package com.stockup.StockUp.Manager.exception;

public class CannotDeleteActiveUserException extends RuntimeException {
  public CannotDeleteActiveUserException(String message) {
    super(message);
  }
}
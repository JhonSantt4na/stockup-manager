package com.stockup.StockUp.Manager.service.auth;

import com.stockup.StockUp.Manager.dto.Auth.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.Auth.security.response.TokenDTO;

import java.util.List;

public interface IAuthService {
	
	TokenDTO login(LoginRequestDTO credentials);
	
	TokenDTO generateTokenForNewUser(String username, List<String> roles);
	
	TokenDTO refreshToken(String username, String refreshToken);
}
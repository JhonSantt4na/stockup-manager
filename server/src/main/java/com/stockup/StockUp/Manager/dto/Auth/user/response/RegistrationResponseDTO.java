package com.stockup.StockUp.Manager.dto.Auth.user.response;

import com.stockup.StockUp.Manager.dto.Auth.security.response.TokenDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDTO {
	private UserResponseDTO user;
	private TokenDTO token;
}
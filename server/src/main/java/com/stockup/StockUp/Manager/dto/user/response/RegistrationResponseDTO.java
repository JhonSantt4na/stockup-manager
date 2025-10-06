package com.stockup.StockUp.Manager.dto.user.response;

import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
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
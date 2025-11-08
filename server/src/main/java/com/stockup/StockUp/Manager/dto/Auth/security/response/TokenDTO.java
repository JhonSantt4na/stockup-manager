package com.stockup.StockUp.Manager.dto.Auth.security.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Boolean authenticated;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private LocalDateTime created;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	private LocalDateTime expiration;
	
	private String accessToken;
	private String refreshToken;
}
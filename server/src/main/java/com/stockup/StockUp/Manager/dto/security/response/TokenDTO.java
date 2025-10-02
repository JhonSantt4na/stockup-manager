package com.stockup.StockUp.Manager.dto.security.response;

import lombok.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String username;
	private Boolean authenticated;
	private Date created;
	private Date expiration;
	private String accessToken;
	private String refreshToken;
}

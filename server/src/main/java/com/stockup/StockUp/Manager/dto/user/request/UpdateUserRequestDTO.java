package com.stockup.StockUp.Manager.dto.user.request;

import java.util.List;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {
	
	@Size(min = 2, max = 100)
	private String fullName;
	
	@Email
	private String email;
	
	@Size(min = 6, max = 100)
	private String password;
	
	private List<String> roles;
}
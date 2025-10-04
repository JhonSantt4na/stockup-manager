package com.stockup.StockUp.Manager.controller.Docs;

import com.stockup.StockUp.Manager.dto.security.request.LoginRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthControllerDocs {
	
	@Operation(
		summary = "Autenticar usuário",
		description = "Autentica o usuário com as credenciais fornecidas e retorna um token JWT.",
		tags = {"Autenticação"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
			@ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<TokenDTO> login(@Valid @RequestBody() LoginRequestDTO credentials);
	
	@Operation(summary = "Logout do usuário autenticado")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Token ausente ou inválido"),
		@ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
	})
	@PostMapping("/logout")
	ResponseEntity<String> logout(HttpServletRequest request);
	
	
	@Operation(
		summary = "Revalidar token (Refresh Token)",
		description = "Gera um novo token de acesso com base no refresh token válido.",
		tags = {"Autenticação"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Novo token gerado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
			@ApiResponse(responseCode = "400", description = "Refresh token inválido", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<TokenDTO> refreshToken(
		@Parameter(description = "Username do usuário") @PathVariable("username") String username,
		@Parameter(description = "Refresh token válido") @RequestHeader("X-Refresh-Token") String refreshToken
	);
}
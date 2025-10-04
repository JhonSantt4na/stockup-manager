package com.stockup.StockUp.Manager.controller.Docs;

import com.stockup.StockUp.Manager.dto.ChangePasswordRequestDTO;
import com.stockup.StockUp.Manager.dto.security.request.RegisterRequestDTO;
import com.stockup.StockUp.Manager.dto.security.response.UserResponseDTO;
import com.stockup.StockUp.Manager.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserControllerDocs {
	
	@Operation(
		summary = "Registrar um novo usuário",
		description = "Registra um novo usuário no sistema. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO credentials);
	
	@Operation(
		summary = "Atualizar dados de um usuário",
		description = "Atualiza os dados de um usuário existente. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<UserResponseDTO> updated(@Valid @RequestBody RegisterRequestDTO credentials);
	
	@Operation(
		summary = "Buscar usuário por username",
		description = "Retorna os dados de um usuário com base no username informado. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<UserResponseDTO> findByUsername(@Parameter(description = "Username do usuário") String username);
	
	@Operation(
		summary = "Excluir usuário por username",
		description = "Remove um usuário com base no username informado. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Void> delete(@Parameter(description = "Username do usuário") String username);
	
	@Operation(
		summary = "Alterar senha do usuário autenticado",
		description = "Permite que o usuário autenticado altere sua senha. Não requer permissão de administrador.",
		tags = {"Usuário - Conta"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Senha alterada com sucesso", content = @Content),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Void> changePassword(
		@Parameter(hidden = true) User authenticatedUser,
		@Valid @RequestBody ChangePasswordRequestDTO dto
	);
	
	@Operation(
		summary = "Listar usuários",
		description = "Retorna uma página de usuários cadastrados. Permite filtro por role. Acesso restrito a administradores.",
		tags = {"Admin - User"}
	)
	ResponseEntity<Page<UserResponseDTO>> listUsers( @RequestParam(required = false) String role, Pageable pageable);
	
	@Operation(
		summary = "Obter perfil do usuário autenticado",
		description = "Retorna os dados do usuário atualmente autenticado. Não expõe a senha.",
		tags = {"User - Profile"}
	)
	@GetMapping("/me")
	ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal User authenticatedUser);
	
	@Operation(
		summary = "Listar roles do usuário",
		description = "Retorna uma lista com todas as roles atribuídas ao usuário informado. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de roles retornada com sucesso", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<List<String>> getUserRoles(@Parameter(description = "Username do usuário") String username);
	
	@Operation(
		summary = "Atribuir roles a um usuário",
		description = "Atribui uma ou mais roles a um usuário específico. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Role atribuídas com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<UserResponseDTO> assignRoles(
		@Parameter(description = "Username do usuário") String username,
		@Valid @RequestBody List<String> roleNames
	);
	
	@Operation(
		summary = "Remover roles de um usuário",
		description = "Remove uma ou mais roles de um usuário. Acesso restrito a administradores.",
		tags = {"Admin - Usuários"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Role removidas com sucesso", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<UserResponseDTO> removeRoles(
		@Parameter(description = "Username do usuário") String username,
		@Valid @RequestBody List<String> roleNames
	);
}

package com.stockup.StockUp.Manager.controller.auth.Docs;

import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.request.RoleUpdateDTO;
import com.stockup.StockUp.Manager.dto.Auth.roles.response.RoleWithUsersDTO;
import com.stockup.StockUp.Manager.model.user.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface RoleControllerDocs {
	
	@Operation(
		summary = "Criar uma nova role",
		description = "Cria uma nova role com o nome informado. Acesso restrito a administradores.",
		tags = {"Admin - Role"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Role criada com sucesso", content = @Content(schema = @Schema(implementation = Role.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "401", description = "Não autorizado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
		}
	)
	ResponseEntity<Role> createRole(@Valid @RequestBody RoleDTO createDto);
	
	@Operation(
		summary = "Atualizar uma role",
		description = "Atualiza uma role existente. Acesso restrito a administradores.",
		tags = {"Admin - Role"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Role atualizada com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "401", description = "Não autorizado"),
			@ApiResponse(responseCode = "404", description = "Role não encontrada"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor")
		}
	)
	ResponseEntity<Role> updateRole(@Valid @RequestBody RoleUpdateDTO updateDto);
	
	@Operation(
		summary = "Buscar role por id",
		description = "Retorna os dados de uma role com base no id informado. Acesso restrito a administradores.",
		tags = {"Admin - Role"}
	)
	ResponseEntity<Role> getRoleById(@Parameter(description = "ID da role") UUID id);
	
	@Operation(
		summary = "Buscar role por nome",
		description = "Retorna os dados de uma role com base no nome informado. Acesso restrito a administradores.",
		tags = {"Admin - Role"}
	)
	ResponseEntity<Role> getRoleByName(@Parameter(description = "Nome da role") String name);
	
	@Operation(
		summary = "Excluir role por nome",
		description = "Desativa logicamente uma role pelo nome. Acesso restrito a administradores.",
		tags = {"Admin - Role"}
	)
	ResponseEntity<Void> deleteRole(@Parameter(description = "Nome da role") String name);
	
	@Operation(
		summary = "Listar todas as roles",
		description = "Retorna uma lista paginada de todas as roles cadastradas, com opção de busca por nome. Acesso restrito a administradores.",
		tags = {"Admin - Role"}
	)
	ResponseEntity<Page<RoleWithUsersDTO>> listRoles(
		Pageable pageable,
		@Parameter(description = "Texto para buscar no nome da role") @RequestParam(required = false) String search
	);
	
	@Operation(
		summary = "Listar roles com usuários",
		description = "Retorna uma lista paginada de todas as roles junto com os usuários associados, com opção de busca por nome. Acesso restrito a administradores.",
		tags = {"Admin - Role"}
	)
	ResponseEntity<Page<RoleWithUsersDTO>> listRolesWithUsers(
		Pageable pageable,
		@Parameter(description = "Texto para buscar no nome da role") @RequestParam(required = false) String search
	);
	
	@Operation(
		summary = "Atribuir permissions a uma role",
		description = "Atribui uma ou mais permissions a uma role existente. Acesso restrito a administradores.",
		tags = {"Admin - Role"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissions atribuídas com sucesso", content = @Content(schema = @Schema(implementation = Role.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Role não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Role> assignPermissions(
		@Parameter(description = "Nome da role") String roleName,
		@Valid @RequestBody List<String> permissionDescriptions
	);
	
	@Operation(
		summary = "Remover permissions de uma role",
		description = "Remove uma ou mais permissions de uma role existente. Acesso restrito a administradores.",
		tags = {"Admin - Role"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissions removidas com sucesso", content = @Content(schema = @Schema(implementation = Role.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Role não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Role> removePermissions(
		@Parameter(description = "Nome da role") String roleName,
		@Valid @RequestBody List<String> permissionDescriptions
	);
	
	@Operation(
		summary = "Listar permissions de uma role",
		description = "Retorna uma lista de todas as permissions atribuídas a uma role. Acesso restrito a administradores.",
		tags = {"Admin - Role"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista de permissions retornada com sucesso", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Role não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<List<String>> getRolePermissions(@Parameter(description = "Nome da role") String roleName);
}

package com.stockup.StockUp.Manager.controller.Docs;

import com.stockup.StockUp.Manager.dto.Roles.RoleCreateDTO;
import com.stockup.StockUp.Manager.dto.Roles.RoleUpdateDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoleControllerDocs {
	@Operation(
		summary = "Criar uma nova permissão (role)",
		description = "Cria uma nova role com a descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Roles"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Permissão criada com sucesso", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> createPermission(@Valid @RequestBody RoleCreateDTO dto);
	
	@Operation(
		summary = "Atualizar uma permissão (role)",
		description = "Atualiza uma role existente. Acesso restrito a administradores.",
		tags = {"Admin - Roles"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissão atualizada com sucesso", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> updatePermission(@Valid @RequestBody RoleUpdateDTO dto);
	
	@Operation(
		summary = "Buscar permissão por descrição",
		description = "Retorna os dados de uma role com base na descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Roles"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissão encontrada", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> getDescriptionRoles(@Parameter(description = "Descrição da role") String description);
	
	@Operation(
		summary = "Excluir permissão por descrição",
		description = "Remove uma role com base na descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Roles"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Permissão excluída com sucesso", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Void> deleteRole(@Parameter(description = "Descrição da role") String description);
	
	@Operation(
		summary = "Listar todas as permissões (roles)",
		description = "Retorna uma lista de todas as roles cadastradas. Acesso restrito a administradores.",
		tags = {"Admin - Roles"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<List<Permission>> listRoles();
}

package com.stockup.StockUp.Manager.controller.Docs;

import com.stockup.StockUp.Manager.dto.permission.request.PermissionCreateDTO;
import com.stockup.StockUp.Manager.dto.permission.request.PermissionUpdateDTO;
import com.stockup.StockUp.Manager.dto.permission.response.PermissionWithRolesDTO;
import com.stockup.StockUp.Manager.model.security.Permission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface PermissionControllerDocs {
	
	@Operation(
		summary = "Criar uma nova permissão",
		description = "Cria uma nova permissão com a descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Permissão"},
		responses = {
			@ApiResponse(responseCode = "201", description = "Permissão criada com sucesso", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> createPermission(@Valid @RequestBody PermissionCreateDTO dto);
	
	@Operation(
		summary = "Atualizar uma permissão",
		description = "Atualiza uma permissão existente. Acesso restrito a administradores.",
		tags = {"Admin - Permissão"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissão atualizada com sucesso", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> updatePermission(@Valid @RequestBody PermissionUpdateDTO dto);
	
	@Operation(
		summary = "Buscar permissão por descrição",
		description = "Retorna os dados de uma permissão com base na descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Permissão"},
		responses = {
			@ApiResponse(responseCode = "200", description = "Permissão encontrada", content = @Content(schema = @Schema(implementation = Permission.class))),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Permission> getPermissionByDescription(@Parameter(description = "Descrição da permissão") String description);
	
	@Operation(
		summary = "Excluir permissão por descrição",
		description = "Remove logicamente uma permissão com base na descrição informada. Acesso restrito a administradores.",
		tags = {"Admin - Permissão"},
		responses = {
			@ApiResponse(responseCode = "204", description = "Permissão excluída com sucesso", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permissão não encontrada", content = @Content),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
		}
	)
	ResponseEntity<Void> deletePermission(@Parameter(description = "Descrição da permissão") String description);
	
	@Operation(
		summary = "Listar permissões paginadas",
		description = "Retorna uma lista de permissões com suas roles associadas, paginadas e ordenáveis",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de permissões obtida com sucesso",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = PermissionWithRolesDTO.class)
				)
			),
			@ApiResponse(responseCode = "401", description = "Não autorizado"),
			@ApiResponse(responseCode = "403", description = "Acesso negado")
		}
	)
	@GetMapping("/list")
	public ResponseEntity<Page<PermissionWithRolesDTO>> listPermissions(
		@Parameter(description = "Número da página (0 baseado)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: description,asc") @RequestParam(defaultValue = "description,asc") String[] sort
	);
	
	@Operation(
		summary = "Listar permissões paginadas",
		description = "Retorna uma lista de permissões com suas roles associadas, paginadas e ordenáveis",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "Lista de permissões obtida com sucesso",
				content = @Content(
					mediaType = "application/json",
					schema = @Schema(implementation = PermissionWithRolesDTO.class)
				)
			),
			@ApiResponse(responseCode = "401", description = "Não autorizado"),
			@ApiResponse(responseCode = "403", description = "Acesso negado")
		}
	)
	@GetMapping("/list")
	public ResponseEntity<Page<PermissionWithRolesDTO>> getAllPermissionsIsActive(
		@Parameter(description = "Número da página (0 baseado)") @RequestParam(defaultValue = "0") int page,
		@Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
		@Parameter(description = "Ordenação, ex: description,asc") @RequestParam(defaultValue = "description,asc") String[] sort
	);
}

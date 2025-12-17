package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentMethodResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "Payment Methods", description = "Gerenciamento dos métodos de pagamento")
public interface PaymentMethodControllerDocs {
	
	@Operation(
		summary = "Criar método de pagamento",
		description = "Cria um novo método de pagamento.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Criado com sucesso",
				content = @Content(schema = @Schema(implementation = PaymentMethodResponseDTO.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida")
		}
	)
	ResponseEntity<PaymentMethodResponseDTO> create(PaymentMethodRequestDTO dto);
	
	@Operation(
		summary = "Listar métodos de pagamento",
		description = "Retorna todos os métodos cadastrados.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Lista retornada")
		}
	)
	ResponseEntity<List<PaymentMethodResponseDTO>> findAll();
	
	@Operation(
		summary = "Buscar método por ID",
		description = "Retorna o método de pagamento com base no ID.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Registro encontrado"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado")
		}
	)
	ResponseEntity<PaymentMethodResponseDTO> findById(UUID id);
	
	@Operation(
		summary = "Atualizar método de pagamento",
		description = "Atualiza os dados do método de pagamento.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado")
		}
	)
	ResponseEntity<PaymentMethodResponseDTO> update(UUID id, PaymentMethodRequestDTO dto);
	
	@Operation(
		summary = "Deletar método de pagamento",
		description = "Remove um método de pagamento pelo ID.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Registro não encontrado")
		}
	)
	void delete(UUID id);
}

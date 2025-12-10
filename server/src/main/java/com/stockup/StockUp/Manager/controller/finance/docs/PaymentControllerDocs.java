package com.stockup.StockUp.Manager.controller.finance.docs;

import com.stockup.StockUp.Manager.dto.finance.payment.PaymentRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.payment.PaymentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Payments", description = "Operações de pagamento")
public interface PaymentControllerDocs {
	
	@Operation(
		summary = "Criar pagamento",
		description = "Registra um novo pagamento.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Pagamento criado",
				content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class)))
		}
	)
	PaymentResponseDTO create(PaymentRequestDTO dto);
	
	@Operation(
		summary = "Buscar pagamento por ID",
		responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404")
		}
	)
	PaymentResponseDTO findById(UUID id);
	
	@Operation(
		summary = "Listar todos os pagamentos",
		responses = @ApiResponse(responseCode = "200")
	)
	List<PaymentResponseDTO> findAll();
	
	@Operation(
		summary = "Listar pagamentos por referência",
		description = "Lista pagamentos associados a um referenceId (pedido/venda)."
	)
	List<PaymentResponseDTO> findByReference(UUID referenceId);
}


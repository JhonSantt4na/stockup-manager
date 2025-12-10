package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.controller.finance.docs.CashRegisterControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.cash.*;
import com.stockup.StockUp.Manager.service.finance.ICashRegisterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash/registers")
@RequiredArgsConstructor
public class CashRegisterController implements CashRegisterControllerDocs {
	
	private final ICashRegisterService service;
	
	@Override
	@PostMapping("/open")
	@ResponseStatus(HttpStatus.CREATED)
	public CashRegisterResponseDTO open(@RequestBody @Valid CashRegisterOpenRequestDTO dto) {
		return service.openRegister(dto);
	}
	
	@Override
	@PostMapping("/{id}/close")
	public CashRegisterResponseDTO close(
		@PathVariable UUID id,
		@RequestBody @Valid CashRegisterCloseRequestDTO dto
	) {
		return service.closeRegister(id, dto);
	}
	
	@Override
	@GetMapping("/{id}")
	public CashRegisterResponseDTO findById(@PathVariable UUID id) {
		return service.findById(id);
	}
}

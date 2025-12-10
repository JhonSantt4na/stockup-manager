package com.stockup.StockUp.Manager.controller.finance;

import com.stockup.StockUp.Manager.controller.finance.docs.CashEntryControllerDocs;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryRequestDTO;
import com.stockup.StockUp.Manager.dto.finance.cash.CashEntryResponseDTO;
import com.stockup.StockUp.Manager.service.finance.ICashEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cash/entries")
@RequiredArgsConstructor
public class CashEntryController implements CashEntryControllerDocs {
	
	private final ICashEntryService service;
	
	@Override
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CashEntryResponseDTO create(@RequestBody @Valid CashEntryRequestDTO dto) {
		return service.create(dto);
	}
	
	@Override
	@GetMapping("/{id}")
	public CashEntryResponseDTO findById(@PathVariable UUID id) {
		return service.findById(id);
	}
	
	@Override
	public List<CashEntryResponseDTO> listByCashRegister(UUID cashRegisterId) {
		return List.of();
	}

//	@Override
//	@GetMapping("/cash-register/{cashRegisterId}")
//	public List<CashEntryResponseDTO> listByCashRegister(@PathVariable UUID cashRegisterId) {
//		return service.listByCashRegister(cashRegisterId);
//	}
}

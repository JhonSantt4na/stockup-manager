package com.stockup.StockUp.Manager.mapper.resolver;

import com.stockup.StockUp.Manager.model.catalog.TaxProfile;
import com.stockup.StockUp.Manager.repository.TaxProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class TaxProfileResolver {
	
	private final TaxProfileRepository repository;
	
	@Named("resolveTaxProfile")
	public TaxProfile resolve(UUID id) {
		if (id == null) return null;
		return repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Tax profile not found: " + id));
	}
}
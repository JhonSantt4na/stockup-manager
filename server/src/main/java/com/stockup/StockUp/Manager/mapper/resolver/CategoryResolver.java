package com.stockup.StockUp.Manager.mapper.resolver;

import com.stockup.StockUp.Manager.model.sales.Category;
import com.stockup.StockUp.Manager.repository.sales.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryResolver {
	
	private final CategoryRepository repository;
	
	@Named("resolveCategory")
	public Category resolve(UUID id) {
		if (id == null) return null;
		return repository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Category not found: " + id));
	}
}

package com.stockup.StockUp.Manager.mapper.catalog;

import com.stockup.StockUp.Manager.dto.Sales.Product.ProductRequestDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductResponseDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductSummaryDTO;
import com.stockup.StockUp.Manager.dto.Sales.Product.ProductUpdateDTO;
import com.stockup.StockUp.Manager.mapper.resolver.CategoryResolver;
import com.stockup.StockUp.Manager.mapper.resolver.TaxProfileResolver;
import com.stockup.StockUp.Manager.model.catalog.Product;
import org.mapstruct.*;

@Mapper(
	componentModel = "spring",
	uses = {CategoryResolver.class, TaxProfileResolver.class},
	unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {
	
	@Mapping(target = "category", source = "categoryId", qualifiedByName = "resolveCategory")
	@Mapping(target = "taxProfile", source = "taxProfileId", qualifiedByName = "resolveTaxProfile")
	Product toEntity(ProductRequestDTO dto);
	
	@Mapping(target = "categoryId", source = "category.id")
	@Mapping(target = "categoryName", source = "category.name")
	@Mapping(target = "taxProfileId", source = "taxProfile.id")
	@Mapping(target = "taxProfileName", source = "taxProfile.name")
	ProductResponseDTO toResponse(Product entity);
	
	ProductSummaryDTO toSummary(Product entity);
	
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "category", source = "categoryId", qualifiedByName = "resolveCategory")
	@Mapping(target = "taxProfile", source = "taxProfileId", qualifiedByName = "resolveTaxProfile")
	void updateFromDto(ProductUpdateDTO dto, @MappingTarget Product entity);
}
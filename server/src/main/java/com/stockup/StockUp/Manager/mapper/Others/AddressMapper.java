package com.stockup.StockUp.Manager.mapper.Others;

import com.stockup.StockUp.Manager.dto.Others.Address.AddressRequestDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressResponseDTO;
import com.stockup.StockUp.Manager.dto.Others.Address.AddressSummaryDTO;
import com.stockup.StockUp.Manager.model.Others.Address;
import com.stockup.StockUp.Manager.model.sales.Customer;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
	componentModel = "spring",
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AddressMapper {
	
	@Mapping(target = "customerId", source = "customer.id")
	AddressResponseDTO toResponse(Address address);
	
	AddressSummaryDTO toSummary(Address address);
	
	@Mapping(target = "customer", source = "customerId", qualifiedByName = "mapCustomer")
	Address toEntity(AddressRequestDTO dto);
	
	@Mapping(target = "customer", ignore = true) // customer não muda em updates
	void updateFromDto(AddressRequestDTO dto, @MappingTarget Address address);
	
	@Named("mapCustomer")
	default Customer mapCustomer(UUID id) {
		if (id == null) return null;
		Customer c = new Customer();
		c.setId(id);
		return c;
	}
}
package com.stockup.StockUp.Manager.mapper.Others;


import com.stockup.StockUp.Manager.dto.Others.Address.AddressDTO;
import com.stockup.StockUp.Manager.model.Others.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
	AddressDTO toDTO(Address address);
}

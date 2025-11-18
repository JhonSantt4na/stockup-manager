package com.stockup.StockUp.Manager.repository.Others;

import com.stockup.StockUp.Manager.model.Others.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
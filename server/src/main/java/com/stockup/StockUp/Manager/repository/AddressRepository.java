package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.customer.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
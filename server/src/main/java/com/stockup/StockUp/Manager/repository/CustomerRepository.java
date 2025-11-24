package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.model.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
	Page<Customer> findAllByEnabledTrue(Pageable pageable);
	Optional<Customer> findByCpfCnpj(String cpfCnpj);
}
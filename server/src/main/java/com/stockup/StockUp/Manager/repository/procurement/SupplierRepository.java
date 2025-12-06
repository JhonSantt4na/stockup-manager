package com.stockup.StockUp.Manager.repository.procurement;

import com.stockup.StockUp.Manager.model.procurement.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
	Optional<Supplier> findByCnpj(String cnpj);
	Optional<Supplier> findByEmail(String email);
	boolean existsByCnpj(String cnpj);
	boolean existsByEmail(String email);
}

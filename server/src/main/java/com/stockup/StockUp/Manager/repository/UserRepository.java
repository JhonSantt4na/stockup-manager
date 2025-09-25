package com.stockup.StockUp.Manager.repository;

import com.stockup.StockUp.Manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	
	@Query("SELECT u FROM User u WHERE u.username =:username")
	User findByUsername(@Param("username") String username);
	
	boolean existsByUsername(String username);
}

package org.web.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Deliverer;

@Repository
public interface DelivererRepository extends JpaRepository<Deliverer, Long> 
{ 
	Deliverer findByUsername(String username);
	Deliverer findByEmail(String email);
	Deliverer findByUsernameAndPassword(String username, String password);
}
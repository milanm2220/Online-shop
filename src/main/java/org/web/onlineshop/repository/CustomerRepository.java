package org.web.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> 
{
	Customer findByUsername(String username);
	Customer findByEmail(String email);
	Customer findByUsernameAndPassword(String username, String password);
}
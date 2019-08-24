package org.web.onlineshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> 
{
	Administrator findByUsername(String username);
	Administrator findByEmail(String email);
	Administrator findByUsernameAndPassword(String username, String password);
}
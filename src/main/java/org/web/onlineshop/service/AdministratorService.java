package org.web.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.model.Administrator;
import org.web.onlineshop.repository.AdministratorRepository;
import org.web.onlineshop.util.UserRole;

@Service
public class AdministratorService 
{
	@Autowired
	private AdministratorRepository administratorRepository;
	
	public Administrator saveWithoutCheck(Administrator administrator)
	{
		administrator.setRole(UserRole.ADMINISTRATOR);
		return this.administratorRepository.save(administrator);
	}
}
package org.web.onlineshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.UserAlreadyExistsException;
import org.web.onlineshop.exceptions.UserNotExistsException;
import org.web.onlineshop.model.Deliverer;
import org.web.onlineshop.repository.DelivererRepository;
import org.web.onlineshop.util.UserRole;

@Service
public class DelivererService 
{
	@Autowired
	private DelivererRepository delivererRepository;
	
	@Autowired
	private UserService userService;
	
	public Deliverer findById(Long id)
	{
		return this.delivererRepository.findById(id).get();
	}
	
	public List<Deliverer> findAll()
	{
		return this.delivererRepository.findAll();
	}
	
	public Deliverer save(Deliverer deliverer)
	{
		if (this.userService.exists(deliverer))
		{
			throw new UserAlreadyExistsException(deliverer.getUsername(), deliverer.getEmail());
		}
		deliverer.setRole(UserRole.DELIVERER);
		return this.delivererRepository.save(deliverer);
	}
	
	public Deliverer saveWithoutCheck(Deliverer deliverer)
	{
		deliverer.setRole(UserRole.DELIVERER);
		return this.delivererRepository.save(deliverer);
	}
	
	public Deliverer update(Deliverer deliverer)
	{
		if (!this.userService.exists(deliverer))
		{
			throw new UserNotExistsException(deliverer.getId(), UserRole.DELIVERER);
		}
		return this.delivererRepository.save(deliverer);
	}
	
	public void delete(Long id)
	{
		this.delivererRepository.deleteById(id);
	}
}
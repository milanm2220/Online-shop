package org.web.onlineshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.UserAlreadyExistsException;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.repository.CustomerRepository;
import org.web.onlineshop.util.UserRole;

@Service
public class CustomerService 
{
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private UserService userService;
	
	public Customer findById(Long id)
	{
		return this.customerRepository.findById(id).get();
	}
	
	public List<Customer> findAll()
	{
		return this.customerRepository.findAll();
	}
	
	public Customer save(Customer customer)
	{
		if (this.userService.exists(customer))
		{
			throw new UserAlreadyExistsException(customer.getUsername(), customer.getEmail());
		}
		customer.setRole(UserRole.CUSTOMER);
		return this.customerRepository.save(customer);
	}
	
	public void delete(Long id)
	{
		this.customerRepository.deleteById(id);
	}
}
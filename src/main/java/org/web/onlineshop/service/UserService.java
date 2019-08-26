package org.web.onlineshop.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.dto.CredentialsDto;
import org.web.onlineshop.exceptions.InvalidCredentialsException;
import org.web.onlineshop.model.Administrator;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.model.Deliverer;
import org.web.onlineshop.model.User;
import org.web.onlineshop.repository.AdministratorRepository;
import org.web.onlineshop.repository.CustomerRepository;
import org.web.onlineshop.repository.DelivererRepository;

@Service
public class UserService 
{
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private DelivererRepository delivererRepository;
	
	@Autowired
	private AdministratorRepository administratorRepository;
	
	@Autowired
	private HttpServletRequest request;
	
	public boolean exists(User user)
	{
		if (customerRepository.findByUsername(user.getUsername()) != null || 
			customerRepository.findByEmail(user.getEmail()) != null ||
			delivererRepository.findByUsername(user.getUsername()) != null ||
			delivererRepository.findByEmail(user.getEmail()) != null ||
			administratorRepository.findByUsername(user.getUsername()) != null ||
			administratorRepository.findByEmail(user.getEmail()) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public User login(CredentialsDto credentialsDto)
	{
		User user = null;
		
		Customer customer = this.customerRepository.findByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (customer != null) { user = customer; }
		
		Deliverer deliverer = this.delivererRepository.findByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (deliverer != null) { user = deliverer; }
		
		Administrator administrator = this.administratorRepository.findByUsernameAndPassword(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (administrator != null) { user = administrator; }
		
		if (user != null)
		{
			request.getSession().setAttribute("user", user);			
		}
		else
		{
			throw new InvalidCredentialsException();
		}
		return user;
	}
	
	public User getLoggedInUser()
	{
		return (User) request.getSession().getAttribute("user");
	}
	
	public void logout()
	{
		request.getSession().removeAttribute("user");
	}
}
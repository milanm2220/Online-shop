package org.web.onlineshop.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.CredentialsDto;
import org.web.onlineshop.dto.CustomerDto;
import org.web.onlineshop.dto.RoleChangeDto;
import org.web.onlineshop.dto.UserDto;
import org.web.onlineshop.exceptions.CantChangeDelivererRoleException;
import org.web.onlineshop.exceptions.UnauthorizedAccessException;
import org.web.onlineshop.model.Administrator;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.model.Deliverer;
import org.web.onlineshop.model.User;
import org.web.onlineshop.service.AdministratorService;
import org.web.onlineshop.service.ArticleService;
import org.web.onlineshop.service.CustomerService;
import org.web.onlineshop.service.DelivererService;
import org.web.onlineshop.service.UserService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.OrderStatus;
import org.web.onlineshop.util.UserRole;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/users")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private DelivererService delivererService;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> getLoggedInUser()
	{
		User user = this.userService.getLoggedInUser();
		if (user == null)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		UserDto userDto = null;
		if (user.getRole() == UserRole.CUSTOMER)
		{
			userDto = modelMapper.map((Customer)user, CustomerDto.class);
		}
		else
		{
			userDto = modelMapper.map(user, UserDto.class);			
		}
		return new ResponseEntity<>(userDto, HttpStatus.OK);	
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto)
	{
		try 
		{
			User user = this.userService.login(credentialsDto);
			UserDto userDto = modelMapper.map(user, UserDto.class);
			return new ResponseEntity<>(userDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logout()
	{
		try 
		{
			this.userService.logout();
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/non_admin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDto>> getNonAdminUsers()
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		List<User> users = this.userService.getNonAdminUsers();
		List<UserDto> userDtos = new ArrayList<>();
		users.stream().forEach(user ->
		{
			userDtos.add(modelMapper.map(user, UserDto.class));
		});
		
		return new ResponseEntity<>(userDtos, HttpStatus.OK);	
	}
	
	@RequestMapping(value = "/non_admin", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> changeRole(@RequestBody RoleChangeDto roleChangeDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		if (roleChangeDto.getOldRole() == roleChangeDto.getNewRole())
		{
			return new ResponseEntity<>(HttpStatus.CONFLICT); 
		}
		
		try 
		{	
			User user = null;
			if (roleChangeDto.getOldRole() == UserRole.CUSTOMER)
			{
				user = this.customerService.findById(roleChangeDto.getId());
			}
			else if (roleChangeDto.getOldRole() == UserRole.DELIVERER)
			{
				user = this.delivererService.findById(roleChangeDto.getId());
				List<Cart> orders = new ArrayList<>(((Deliverer)user).getDeliveries());
				for (Cart order : orders)
				{
					if (order.getState() != null && order.getState() == OrderStatus.DELIVERY_IN_PROGRESS)
					{
						throw new CantChangeDelivererRoleException(user.getUsername());
					}
				}
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			if (roleChangeDto.getNewRole() == UserRole.ADMINISTRATOR)
			{
				Administrator administrator = this.modelMapper.map(user, Administrator.class);
				administrator.setId(null);
				this.administratorService.saveWithoutCheck(administrator);
			}
			else if (roleChangeDto.getNewRole() == UserRole.DELIVERER)
			{
				Deliverer deliverer = this.modelMapper.map(user, Deliverer.class);
				deliverer.setId(null);
				this.delivererService.saveWithoutCheck(deliverer);
			}
			else
			{
				Customer customer = this.modelMapper.map(user, Customer.class);
				customer.setId(null);
				this.customerService.saveWithoutCheck(customer);
			}
			
			if (roleChangeDto.getOldRole() == UserRole.CUSTOMER)
			{
				List<Article> articles = new ArrayList<>(((Customer)user).getFavoriteArticles());
				for (Article article : articles) 
				{
					article.getCustomers().remove((Customer)user);
					this.articleService.update(article);	
				}
				this.customerService.delete(roleChangeDto.getId());
			}
			else if (roleChangeDto.getOldRole() == UserRole.DELIVERER)
			{
				this.delivererService.delete(roleChangeDto.getId());
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}
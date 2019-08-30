package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.CartDto;
import org.web.onlineshop.dto.UserDto;
import org.web.onlineshop.exceptions.DelivererHasDeliveryInProgressException;
import org.web.onlineshop.exceptions.UnauthorizedAccessException;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.model.Deliverer;
import org.web.onlineshop.service.CartService;
import org.web.onlineshop.service.CustomerService;
import org.web.onlineshop.service.DelivererService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.OrderStatus;
import org.web.onlineshop.util.UserRole;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/deliverers")
public class DelivererController 
{
	@Autowired
	private DelivererService delivererService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserDto>> getDeliverers()
	{
		List<Deliverer> deliverers = this.delivererService.findAll();
		List<UserDto> delivererDtos = new ArrayList<>();
		deliverers.stream().forEach(customer -> 
		{
			delivererDtos.add(this.modelMapper.map(customer, UserDto.class));
		});
		return new ResponseEntity<>(delivererDtos, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> add(@RequestBody UserDto delivererDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Deliverer deliverer = this.modelMapper.map(delivererDto, Deliverer.class);
			deliverer = this.delivererService.save(deliverer);
			delivererDto = this.modelMapper.map(deliverer, UserDto.class);
			return new ResponseEntity<>(delivererDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserDto> update(@RequestBody UserDto delivererDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Deliverer deliverer = this.modelMapper.map(delivererDto, Deliverer.class);
			deliverer = this.delivererService.update(deliverer);
			delivererDto = this.modelMapper.map(deliverer, UserDto.class);
			return new ResponseEntity<>(delivererDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Deliverer deliverer = this.delivererService.findById(id);
			List<Cart> orders = new ArrayList<>(deliverer.getDeliveries());
			for (Cart order : orders)
			{
				if (order.getState() != null && order.getState() == OrderStatus.DELIVERY_IN_PROGRESS)
				{
					throw new DelivererHasDeliveryInProgressException(deliverer.getUsername());
				}
			}
			this.delivererService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/take_over/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> takeOverOrder(@PathVariable Long id, @RequestBody CartDto cartDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.DELIVERER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{			
			Deliverer deliverer = this.delivererService.findById(id);
			Cart cart = this.modelMapper.map(cartDto, Cart.class);
			if (this.cartService.takeOverOrder(cart, deliverer))
			{
				deliverer.getDeliveries().add(cart);
				this.delivererService.update(deliverer);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/deliver/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deliverOrder(@PathVariable Long id, @RequestBody CartDto cartDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.DELIVERER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{			
			Cart cart = this.modelMapper.map(cartDto, Cart.class);
			
			if (cart.getDeliverer() == null || !cart.getDeliverer().getId().equals(id) || cart.getState() != OrderStatus.DELIVERY_IN_PROGRESS)
			{
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			if (this.cartService.deliverOrder(cart))
			{	
				if (cart.getTotalPrice() > Constants.PRICE_LIMIT_TO_GET_BONUS_POINT)
				{
					Customer customer = this.customerService.findById(cart.getCustomer().getId());
					if (customer.getBonusPoints() == null)
					{
						customer.setBonusPoints(1);
					}
					else
					{					
						customer.setBonusPoints(customer.getBonusPoints() + 1);					
					}
					this.customerService.update(customer);					
				}
				
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/cancel/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> cancelOrder(@PathVariable Long id, @RequestBody CartDto cartDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.DELIVERER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{			
			Cart cart = this.modelMapper.map(cartDto, Cart.class);
			
			if (cart.getDeliverer() == null || !cart.getDeliverer().getId().equals(id) || cart.getState() != OrderStatus.DELIVERY_IN_PROGRESS)
			{
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			
			if (this.cartService.cancelOrder(cart))
			{	
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/all/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartDto>> getOrders(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.DELIVERER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Deliverer deliverer = this.delivererService.findById(id);
			List<Cart> orders = new ArrayList<>(deliverer.getDeliveries());
			List<CartDto> orderDtos = new ArrayList<>();
			orders.stream().forEach(order ->
			{
				if (order.getState() != null && order.getState() != OrderStatus.DELIVERY_IN_PROGRESS)
				{
					orderDtos.add(modelMapper.map(order, CartDto.class));					
				}
			});
			return new ResponseEntity<>(orderDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/in_progress/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartDto> getOrderInProgress(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.DELIVERER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Deliverer deliverer = this.delivererService.findById(id);
			List<Cart> orders = new ArrayList<>(deliverer.getDeliveries());
			CartDto orderDto = null;
			for (Cart order : orders)
			{
				if (order.getState() != null && order.getState() == OrderStatus.DELIVERY_IN_PROGRESS)
				{
					orderDto = modelMapper.map(order, CartDto.class);
					break;
				}
			}				
			return new ResponseEntity<>(orderDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}
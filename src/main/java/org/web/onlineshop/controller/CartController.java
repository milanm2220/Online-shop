package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.CartDto;
import org.web.onlineshop.dto.ItemDto;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Item;
import org.web.onlineshop.service.CartService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.OrderStatus;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/carts")
public class CartController 
{		
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ItemDto>> getCartItems(@PathVariable Long id)
	{
		try
		{
			Cart cart = this.cartService.findById(id);
			List<Item> items = new ArrayList<>(cart.getItems());
			List<ItemDto> itemDtos = new ArrayList<>();
			items.stream().forEach(item ->
			{
				itemDtos.add(modelMapper.map(item, ItemDto.class));
			});
			return new ResponseEntity<>(itemDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/ordered", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartDto>> getOrdersInOrderedState()
	{
		try
		{
			List<Cart> orders = this.cartService.findByState(OrderStatus.ORDERED);
			List<CartDto> orderDtos = new ArrayList<>();
			orders.stream().forEach(order ->
			{
				orderDtos.add(modelMapper.map(order, CartDto.class));					
			});
			return new ResponseEntity<>(orderDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}
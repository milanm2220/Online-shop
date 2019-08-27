package org.web.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.CartNotExistsException;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Item;
import org.web.onlineshop.repository.CartRepository;
import org.web.onlineshop.repository.ItemRepository;

@Service
public class CartService 
{
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public Cart findById(Long id)
	{
		return this.cartRepository.findById(id).get();
	}
	
	public Cart save(Cart cart)
	{
		return this.cartRepository.save(cart);
	}
	
	public Cart update(Cart cart)
	{
		if (this.cartRepository.existsById(cart.getId()))
		{
			return this.cartRepository.save(cart);			
		}
		else
		{
			throw new CartNotExistsException(cart.getId());
		}
	}
	
	public void delete(Long id)
	{
		this.cartRepository.deleteById(id);
	}
	
	public boolean addItemToCart(Cart cart, Item item)
	{
		if (cart != null && item != null)
		{
			try
			{
				item = this.save(item);
				cart.getItems().add(item);
				this.update(cart);
				return true;
			}
			catch(Exception e)
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public Item save(Item item)
	{
		return this.itemRepository.save(item);
	}
}
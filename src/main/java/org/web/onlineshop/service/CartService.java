package org.web.onlineshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.CartNotExistsException;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Deliverer;
import org.web.onlineshop.model.Item;
import org.web.onlineshop.repository.CartRepository;
import org.web.onlineshop.repository.ItemRepository;
import org.web.onlineshop.util.OrderStatus;

@Service
public class CartService 
{
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Cart> findByState(OrderStatus orderStatus)
	{
		return this.cartRepository.findByState(orderStatus);
	}
	
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
	
	public Item addItemToCart(Cart cart, Item item)
	{
		if (cart != null && item != null)
		{
			try
			{
				item.setCart(cart);
				item = this.saveItem(item);
				cart.getItems().add(item);
				this.update(cart);
				return item;
			}
			catch(Exception e) { return null; }
		}
		else
		{
			return null;
		}
	}
	
	public boolean dropItemFromCart(Cart cart, Item item)
	{
		if (cart != null && item != null)
		{
			try
			{
				cart.getItems().remove(item);
				this.update(cart);
				this.deleteItem(item.getId());
				return true;
			}
			catch(Exception e) { return false; }
		}
		else
		{
			return false;
		}
	}
	
	public boolean takeOverOrder(Cart cart, Deliverer deliverer)
	{
		if (cart != null && deliverer != null)
		{
			try
			{
				cart.setDeliverer(deliverer);
				cart.setState(OrderStatus.DELIVERY_IN_PROGRESS);
				this.update(cart);
				return true;
			}
			catch(Exception e) { return false; }
		}
		else
		{
			return false;
		}
	}
	
	public boolean deliverOrder(Cart cart)
	{
		if (cart != null)
		{
			try
			{
				cart.setState(OrderStatus.DELIVERED);
				this.update(cart);
				return true;
			}
			catch(Exception e) { return false; }
		}
		else
		{
			return false;
		}
	}
	
	public boolean cancelOrder(Cart cart)
	{
		if (cart != null)
		{
			try
			{
				cart.setState(OrderStatus.CANCELED);
				this.update(cart);
				return true;
			}
			catch(Exception e) { return false; }
		}
		else
		{
			return false;
		}
	}
	
	public Item saveItem(Item item)
	{
		return this.itemRepository.save(item);
	}
	
	public void deleteItem(Long id)
	{
		this.itemRepository.deleteById(id);
	}
}
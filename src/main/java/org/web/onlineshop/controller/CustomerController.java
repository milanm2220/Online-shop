package org.web.onlineshop.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.ArticleDto;
import org.web.onlineshop.dto.CartDto;
import org.web.onlineshop.dto.CustomerDto;
import org.web.onlineshop.dto.ItemDto;
import org.web.onlineshop.exceptions.UnauthorizedAccessException;
import org.web.onlineshop.exceptions.UserNotExistsException;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.Cart;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.model.Item;
import org.web.onlineshop.service.ArticleService;
import org.web.onlineshop.service.CartService;
import org.web.onlineshop.service.CustomerService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.OrderStatus;
import org.web.onlineshop.util.UserRole;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/customers")
public class CustomerController
{
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDto> registerCustomer(@RequestBody CustomerDto customerDto)
	{
		try
		{
			Customer customer = this.modelMapper.map(customerDto, Customer.class);
			customer = this.customerService.save(customer);
			customerDto = this.modelMapper.map(customer, CustomerDto.class);
			return new ResponseEntity<>(customerDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/favourite_articles/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> getFavouriteArticles(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			List<Article> articles = new ArrayList<>(customer.getFavoriteArticles());
			List<ArticleDto> articleDtos = new ArrayList<>();
			articles.stream().forEach(article ->
			{
				articleDtos.add(modelMapper.map(article, ArticleDto.class));
			});
			return new ResponseEntity<>(articleDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/favourite_articles/add/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addFavouriteArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Article article = this.articleService.findById(articleDto.getId());
			article.getCustomers().add(customer);
			this.articleService.update(article);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/favourite_articles/remove/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeArticleFromFavourites(@PathVariable Long id, @RequestBody ArticleDto articleDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Article article = this.articleService.findById(articleDto.getId());
			article.getCustomers().remove(customer);
			this.articleService.update(article);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CartDto> getCart(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Cart cart = customer.getCart();
			CartDto cartDto = null;
			if (cart != null)
			{
				cartDto = modelMapper.map(cart, CartDto.class);
			}
			return new ResponseEntity<>(cartDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/add/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemDto> addItemToCart(@PathVariable Long id, @RequestBody ItemDto itemDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Cart cart = customer.getCart();
			if (cart == null)
			{
				cart = new Cart();
				cart.setCustomer(customer);
				cart = this.cartService.save(cart);
				customer.setCart(cart);
				this.customerService.update(customer);
			}
			Item item = modelMapper.map(itemDto, Item.class);
			item = this.cartService.addItemToCart(cart, item);
			if (item != null)
			{
				itemDto = modelMapper.map(item, ItemDto.class);
				return new ResponseEntity<>(itemDto, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/drop/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> dropItemFromCart(@PathVariable Long id, @RequestBody ItemDto itemDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Cart cart = customer.getCart();
			if (cart == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			Item item = modelMapper.map(itemDto, Item.class);
			if (this.cartService.dropItemFromCart(cart, item))
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
	
	@RequestMapping(value = "cart/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> order(@PathVariable Long id, @RequestParam(required = false, value = "bonusPoints") Integer bonusPoints)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{	
			Customer customer = this.customerService.findById(id);
			
			if (bonusPoints != null && (bonusPoints > customer.getBonusPoints() || bonusPoints > Constants.MAXIMUM_BONUS_POINTS))
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			Cart cart = customer.getCart();
			if (cart == null)
			{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
			double totalPrice = cart.getItems().stream().mapToDouble(item -> 
			{
				Integer discount = item.getArticle().getDiscount();
				double price = item.getArticle().getPrice();
				if (discount != null && discount > 0)
				{
					price *= ((double)(100 - discount)) / 100d;
				}
				return price * item.getAmount();
			}).sum();
			totalPrice = (bonusPoints != null) ? (totalPrice * (100 - bonusPoints * 2)) / 100 : totalPrice;			
			cart.setTotalPrice(totalPrice);
			cart.setState(OrderStatus.ORDERED);
			cart.setTimestamp(LocalDateTime.now());
			cart = this.cartService.update(cart);
			
			if (bonusPoints != null)
			{
				customer.setBonusPoints(customer.getBonusPoints() - bonusPoints);
			}
			
			customer.setCart(null);
			customer.getPreviousPurchases().add(cart);
			this.customerService.update(customer);
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/cart/all/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CartDto>> getOrders(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			List<Cart> orders = new ArrayList<>(customer.getPreviousPurchases());
			List<CartDto> orderDtos = new ArrayList<>();
			orders.stream().forEach(order ->
			{
				if (order.getState() != null)
				{
					orderDtos.add(modelMapper.map(order, CartDto.class));					
				}
			});
			return new ResponseEntity<>(orderDtos, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/bonusPoints/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> getBonusPoints(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.CUSTOMER || !request.getSession().getAttribute("id").equals(id))
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			Customer customer = this.customerService.findById(id);
			Integer bonusPoints = customer.getBonusPoints();
			return new ResponseEntity<>(bonusPoints, HttpStatus.OK);
		}
		catch(Exception e) { throw new UserNotExistsException(id, UserRole.CUSTOMER); }
	}
}
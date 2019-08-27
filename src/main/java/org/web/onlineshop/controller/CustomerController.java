package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.web.onlineshop.dto.ArticleDto;
import org.web.onlineshop.dto.CustomerDto;
import org.web.onlineshop.exceptions.CustomerNotExistsException;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.Customer;
import org.web.onlineshop.service.CustomerService;
import org.web.onlineshop.util.Constants;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/customers")
public class CustomerController
{
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id)
	{
		try
		{
			Customer customer = this.customerService.findById(id);
			CustomerDto customerDto = this.modelMapper.map(customer, CustomerDto.class);
			return new ResponseEntity<>(customerDto, HttpStatus.OK);
		}
		catch(Exception e)
		{
			throw new CustomerNotExistsException(id);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<CustomerDto>> getAllCustomers()
	{
		List<Customer> customers = this.customerService.findAll();
		List<CustomerDto> customerDtos = new ArrayList<>();
		customers.stream().forEach(customer -> 
		{
			customerDtos.add(this.modelMapper.map(customer, CustomerDto.class));
		});
		
		return new ResponseEntity<>(customerDtos, HttpStatus.OK);
	}
	
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
	
	@RequestMapping(value = "/favourite_articles/add/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addFavouriteArticle(@PathVariable Long id, @RequestBody ArticleDto articleDto)
	{
		try
		{
			Customer customer = this.customerService.findById(id);
			Article article = modelMapper.map(articleDto, Article.class);
			customer.getFavoriteArticles().add(article);
			this.customerService.update(customer);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/favourite_articles/remove/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> removeArticleFromFavourites(@PathVariable Long id, @RequestBody ArticleDto articleDto)
	{
		try
		{
			Customer customer = this.customerService.findById(id);
			Article article = modelMapper.map(articleDto, Article.class);
			customer.getFavoriteArticles().remove(article);
			this.customerService.update(customer);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
}
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
import org.web.onlineshop.dto.CustomerDto;
import org.web.onlineshop.exceptions.CustomerNotExistsException;
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
}